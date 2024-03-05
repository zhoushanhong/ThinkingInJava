# k8s基本操作
## 如何解决问题的帖子
https://www.codenong.com/js9ff1f21fd788/

## namespace
### 常用命令
```bash
kubectl get namespace # 查询命名空间
kubectl create namespace test # 创建命名空间test
kubectl delete namespace test # 删除命名空间test
kubectl config set-context --current --namespace=test # 指定默认命名空间test
kubectl config view | grep namespace # 验证当前命名空间
```

## 通过资源的方式创建namespace
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: test
```

```bash
kubectl apply -f create-namespace.yaml
```

## pod
pod是k8s中能被创建管理的最小可部署单元

### 常用命令
```bash
kubectl run nginx --image=nginx:1.14.2 -n test # 在test命名空间下创建nginx
kubectl get pod -n test # 查看test命名空间下的pod
kubectl describe pod nginx -n test # 查看nginx的pod的详情(排查问题) 主要关注events
docker images # 查看本机的所有docker镜像
kubectl get pod -owide -n test # 查看test命名空间下pod的ip信息
curl <ip> # 尝试访问pod(nginx)
kubectl logs <pod-name> # 查看pod的运行日志
kubectl delete pod <pod-name> # 删除pod
kubectl exec -it <pod-name> -c <image-name> -- sh # 进入pod容器内部
```

### 通过资源的方式创建pod(缺少namespace)
```yaml
apiVersion: v1
kind: Pod
metadata:
	labels:
		run: mynginx
	name: mynginx
spec:
	containers:
	- name: nginx
	  image: nginx:1.14.2
	  ports:
	  - containerPort: 80	
```	

### 一个pod创建多个容器(内部共享网络)
```yaml
apiVersion: v1
kind: Pod
metadata:
	labels:
		run: myapp
	name: myapp
spec:
	containers:
	- image: nginx:1.14.2		
	  name: nginx
	- image: tomcat:9.0.55		
	  name: tomcat
```

## deployment(无状态服务 本地微服务) 
deployment使pod拥有多个副本 自愈 扩缩容的能力

### 平级对象
statefulset(有状态服务 需要考虑共享存储 mysql, redis) 
daemonset(日志搜集)  
job(定时任务) 


### 常用命令
```bash
kubectl create deployment my-tomcat --image=tomcat:9.0.55 --replicas=3 -n test # 创建一个deployment 有三个副本
kubectl get deployment -n test # 查询命名空间下的deployment
kubectl delete pod my-tomcat-7f776c8676-6zmvq -n test # 删除deployment下的pod
kubectl get pod -w -n test # 监听pod
kubectl exec -it <pod-name> -c <image-name> -- env # 使用env命令查看环境变量
kubectl exec -it <pod-name> -c <image-name> -- ls # 查看容器更目录下的内容
kubectl edit deployment my-tomcat # 通过命令行把已经存在的deployment反向生成资源yaml	
kubectl scale --replicas=5 deployment my-tomcat # 扩容到5个pod副本
kubectl set image deployment my-tomcat tomcat=tomcat:10.1.11 --record # 升级image
kubectl rollout history deployment my-tomcat # 查看可供回退的image版本
kubectl rollout undo deployment my-tomcat # 回退到上一个版本
kubectl rollout undo deployment/my-tomcat --to-revision=2 # 回退到指定版本
```


## service
service为一组pod提供网络服务

### 暴露的方式
ClusterIP: 不能对外暴露
NodePort: 能暴露
LoadBalancer: 能暴露 并且能固定IP
ExternalName: 

### 常用命令
```bash
kubectl expose deployment my-tomcat --name=tomcat --port=8080 --type=NodePort -n test # 创建service 或者说 暴露deployment
kubectl get service -n test # 查看service
kubectl edit svc tomcat # 查看资源清单
kubectl get svc tomcat -oyaml # 查看资源清单
```

### service的资源清单
port指的是service的端口
targetPort指的是pod暴露出来的端口

## volume
提供共享存储功能 防止deployment重新拉起pod后数据丢失
configMap: 键值对配置
emptyDir: 空目录 临时数据
local: 本地
nfs: 网络目录实现共享
secret: 存储敏感信息

### 资源清单关键字
volumeMounts
volumes

### 创建静态PV
```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv01-10m
spec:
  capacity:
    storage: 10M  
  accessModes:   
    - ReadWriteMany
  storageClassName: nfs
  nfs:
    path: /nfs/data/test/01
    server: 192.168.71.54
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv02-1gi
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteMany 
  storageClassName: nfs
  nfs:
    path: /nfs/data/test/02
    server: 192.168.71.54
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv03-3gi
spec:
  capacity:
    storage: 3Gi
  accessModes:
    - ReadWriteMany
  storageClassName: nfs
  nfs:
    path: /nfs/data/test/03
    server: 192.168.71.54
```


```bash
kubectl apply -f create-static-pv.yaml

kubectl get pv
```

### 创建持久化申请 防止server的ip泄露
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nginx-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 200Mi
  storageClassName: nfs
```

```bash
kubectl get pv
```

### 创建数据外挂的nginx
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx-deploy-pvc
  name: nginx-deploy-pvc
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nginx-deploy-pvc
  template:
    metadata:
      labels:
        app: nginx-deploy-pvc
    spec:
      containers:
      - image: nginx:1.14.2	
        name: nginx
        volumeMounts:
        - name: html
          mountPath: /usr/share/nginx/html
      volumes:
        - name: html
          persistentVolumeClaim:
            claimName: nginx-pvc
```

```bash
kubectl get pvc # 切换到对应目录 然后创建新的index.html

echo "hello dendy" >> index.html
```
### 动态供应
```yaml
## 创建了一个存储类
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: nfs-storage
  annotations:
    storageclass.kubernetes.io/is-default-class: "true"
provisioner: k8s-sigs.io/nfs-subdir-external-provisioner
parameters:
  archiveOnDelete: "true"  ## 删除pv的时候，pv的内容是否要备份

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nfs-client-provisioner
  labels:
    app: nfs-client-provisioner
  namespace: test
spec:
  replicas: 1
  strategy:
    type: Recreate
  selector:
    matchLabels:
      app: nfs-client-provisioner
  template:
    metadata:
      labels:
        app: nfs-client-provisioner
    spec:
      serviceAccountName: nfs-client-provisioner
      containers:
        - name: nfs-client-provisioner
          image: registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/nfs-subdir-external-provisioner:v4.0.2
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              value: k8s-sigs.io/nfs-subdir-external-provisioner
            - name: NFS_SERVER
              value: 192.168.71.54 ## 指定自己nfs服务器地址
            - name: NFS_PATH  
              value: /nfs/data/test  ## nfs服务器共享的目录
      volumes:
        - name: nfs-client-root
          nfs:
            server: 192.168.71.54
            path: /nfs/data/test
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nfs-client-provisioner
  namespace: test
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: nfs-client-provisioner-runner
rules:
  - apiGroups: [""]
    resources: ["nodes"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["persistentvolumes"]
    verbs: ["get", "list", "watch", "create", "delete"]
  - apiGroups: [""]
    resources: ["persistentvolumeclaims"]
    verbs: ["get", "list", "watch", "update"]
  - apiGroups: ["storage.k8s.io"]
    resources: ["storageclasses"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["events"]
    verbs: ["create", "update", "patch"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: run-nfs-client-provisioner
subjects:
  - kind: ServiceAccount
    name: nfs-client-provisioner
    namespace: test
roleRef:
  kind: ClusterRole
  name: nfs-client-provisioner-runner
  apiGroup: rbac.authorization.k8s.io
---
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: leader-locking-nfs-client-provisioner
  namespace: test
rules:
  - apiGroups: [""]
    resources: ["endpoints"]
    verbs: ["get", "list", "watch", "create", "update", "patch"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: leader-locking-nfs-client-provisioner
  namespace: test
subjects:
  - kind: ServiceAccount
    name: nfs-client-provisioner
    namespace: test
roleRef:
  kind: Role
  name: leader-locking-nfs-client-provisioner
  apiGroup: rbac.authorization.k8s.io
```

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nginx-dy-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 200Mi
  storageClassName: nfs-storage
```

## ingress
k8s的网关

