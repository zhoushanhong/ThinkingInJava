全部使用root用户权限操作 所有节点都需要操作

# 固定ip 防止虚拟机重启后ip变化
sudo vim /etc/sysconfig/network-scripts/ifcfg-ens33

IPADDR=192.168.71.54 
NETMASK=255.255.255.0
GATEWAY=192.168.71.1
DNS1=116.228.111.118
DNS2=180.168.255.18

sudo systemctl restart network

# 设置主机名
hostnamectl set-hostname k8s-master

# 设置时区
timedatectl set-timezone Asia/Shanghai

# 设置集群
cat >> /etc/hosts << EOF
192.168.71.54 k8s-master
192.168.71.55 k8s-node1
192.168.71.56 k8s-node2
192.168.71.57 k8s-node3
192.168.71.58 k8s-node4
EOF

# 永久关闭seLinux(需重启系统生效)
setenforce 0
sed -i 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/selinux/config

# 永久关闭swap(需重启系统生效)
swapoff -a  # 临时关闭
sed -i 's/.*swap.*/#&/g' /etc/fstab # 永久关闭

# 关闭防火墙
systemctl disable firewalld && systemctl stop firewalld

# 关闭NetworkManager
systemctl disable NetworkManager && systemctl stop NetworkManager

# 加载IPVS模块
yum -y install ipset ipvsadm

cat > /etc/sysconfig/modules/ipvs.modules <<EOF
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack
EOF

modprobe -- nf_conntrack

chmod 755 /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules && lsmod | grep -e ip_vs -e nf_conntrack

# 开启br_netfilter、ipv4 路由转发
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

sudo modprobe overlay

sudo modprobe br_netfilter

# 设置所需的 sysctl 参数，参数在重新启动后保持不变
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# 应用 sysctl 参数而不重新启动
sudo sysctl --system

# 查看是否生效
lsmod | grep br_netfilter
lsmod | grep overlay

sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward

# 设置资源配置文件
cat >> /etc/security/limits.conf << 'EOF'
* soft nofile 100001
* hard nofile 100002
root soft nofile 100001
root hard nofile 100002
* soft memlock unlimited
* hard memlock unlimited
* soft nproc 254554
* hard nproc 254554
* soft sigpending 254554
* hard sigpending 254554
EOF

grep -vE "^\s*#" /etc/security/limits.conf

ulimit -a

# 安装docker
wget -O /etc/yum.repos.d/docker-ce.repo https://download.docker.com/linux/centos/docker-ce.repo

sed -i 's+download.docker.com+mirrors.cloud.tencent.com/docker-ce+' /etc/yum.repos.d/docker-ce.repo

yum makecache fast

yum -y install docker-ce-24.0.6 docker-ce-cli-24.0.6 containerd.io

systemctl enable docker --now

cat > /etc/docker/daemon.json << 'EOF'
{
 "registry-mirrors": [
  "https://mirror.ccs.tencentyun.com"
  ],
 "insecure-registries": [
  "https://harbor.huanghuanhui.cloud"
  ],
 "exec-opts": [
  "native.cgroupdriver=systemd"
  ],
 "log-driver": "json-file",
 "log-opts": {
  "max-size": "100m"
  },
 "storage-driver": "overlay2",
 "data-root": "/var/lib/docker"
}
EOF

systemctl daemon-reload && systemctl restart docker

yum -y install bash-completion

source /etc/profile.d/bash_completion.sh

# 安装k8s kubeadm-1.23.17、kubelet-1.23.17、kubectl-1.23.17
cat > /etc/yum.repos.d/kubernetes.repo <<EOF
[kubernetes]
name=kubernetes
baseurl=https://mirrors.tuna.tsinghua.edu.cn/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=0
EOF

yum -y install kubeadm-1.23.17-0 kubelet-1.23.17-0 kubectl-1.23.17-0

systemctl enable --now kubelet

# 安装nfs-server
yum install -y nfs-utils

master节点执行
```bash
echo "/nfs/data/ *(insecure,rw,sync,no_root_squash)" > /etc/exports

# 创建共享目录 启动nfs服务
mkdir -p /nfs/data 

systemctl enable rpcbind
systemctl enable nfs-server
systemctl start rpcbind
systemctl start nfs-server

# 使配置生效
exportfs -r

# 检查配置是否生效
exportfs
```

node节点执行
```bash
showmount -e 192.168.71.54
mkdir -p /nfs/data
mount -t nfs 192.168.71.54:/nfs/data /nfs/data
```



# 初始化集群(以下全是master节点的操作)
mkdir ~/kubeadm_init && cd ~/kubeadm_init

kubeadm config print init-defaults > kubeadm-init.yaml

```txt
cat > ~/kubeadm_init/kubeadm-init.yaml << EOF
apiVersion: kubeadm.k8s.io/v1beta2
bootstrapTokens:
- groups:
  - system:bootstrappers:kubeadm:default-node-token
  token: abcdef.0123456789abcdef
  ttl: 24h0m0s
  usages:
  - signing
  - authentication
kind: InitConfiguration
localAPIEndpoint:
  advertiseAddress: 192.168.71.54 # 修改自己的master节点ip
  bindPort: 6443
nodeRegistration:
  criSocket: /var/run/dockershim.sock
  name: master
  taints:
  - effect: "NoSchedule"
    key: "node-role.kubernetes.io/master"
---
apiServer:
  timeoutForControlPlane: 4m0s
apiVersion: kubeadm.k8s.io/v1beta2
certificatesDir: /etc/kubernetes/pki
clusterName: kubernetes
controllerManager: {}
dns:
  type: CoreDNS
etcd:
  local:
    dataDir: /var/lib/etcd
imageRepository: registry.aliyuncs.com/google_containers
kind: ClusterConfiguration
kubernetesVersion: v1.23.17
networking:
  dnsDomain: cluster.local
  serviceSubnet: 10.96.0.0/12
  podSubnet: 10.244.0.0/16  
scheduler: {}
--- 
apiVersion: kubeproxy.config.k8s.io/v1alpha1
kind: KubeProxyConfiguration 
mode: ipvs 
--- 
apiVersion: kubelet.config.k8s.io/v1beta1
kind: KubeletConfiguration 
cgroupDriver: systemd
EOF
```

# 查看所需镜像列表
kubeadm config images list --config kubeadm-init.yaml

# 预拉取镜像
kubeadm config images pull --config kubeadm-init.yaml

# 初始化
kubeadm init --config=kubeadm-init.yaml | tee kubeadm-init.log

# 加入集群
kubeadm join 192.168.71.54:6443 --token abcdef.0123456789abcdef \
        --discovery-token-ca-cert-hash sha256:3d3ac9adf37ed7ae6cf0df70a2e47fd92ce2dc000678cd82ced331ea0291eeda

# 生成新的令牌
kubeadm token create --print-join-command

# 配置 kubectl
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 安装k8s集群网络(calico)
k8s和calico对应关系
https://docs.tigera.io/archive/v3.25/getting-started/kubernetes/requirements

mkdir -p ~/calico-yml

cd ~/calico-yml && wget https://github.com/projectcalico/calico/raw/v3.25.1/manifests/calico.yaml

# 修改CIDR
```shell
sed -i 's/192\.168/10\.244/g' calico.yaml
sed -i 's/# \(- name: CALICO_IPV4POOL_CIDR\)/\1/' calico.yaml
sed -i 's/# \(\s*value: "10.244.0.0\/16"\)/\1/' calico.yaml
```

# 指定网卡
```shell
sed -i '/value: "k8s,bgp"/a \            - name: IP_AUTODETECTION_METHOD' \calico.yaml
sed -i '/- name: IP_AUTODETECTION_METHOD/a \              value: "interface=ens33"' \calico.yaml
```

kubectl apply -f ~/calico-yml/calico.yaml

# 文件修改后的样子
```
1 修改CIDR
- name: CALICO_IPV4POOL_CIDR
  value: "10.244.0.0/16"

2 指定网卡
# Cluster type to identify the deployment type
  - name: CLUSTER_TYPE
  value: "k8s,bgp"

# 下面添加
  - name: IP_AUTODETECTION_METHOD
    value: "interface=ens33"
    # ens33为本地网卡名字（自己机器啥网卡就改啥）
```

# coredns解析测试是否正常
kubectl run -it --rm dns-test --image=busybox:1.28.4 sh

nslookup kubernetes

# 安装ingress
wget https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.3.1/deploy/static/provider/baremetal/deploy.yaml
registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v1.3.1
registry.cn-hangzhou.aliyuncs.com/google_containers/nginx-ingress-controller:v1.3.1