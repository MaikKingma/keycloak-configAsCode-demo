# Deploy Keycloak for development
## Steps
1. Create a Kind cluster
```console
kind create cluster
```
2. (Optional) Load Keycloak local docker image.
```console
kind load docker-image navify-registry.jfrog.io/edge-platform/edge-iam-keycloak:0.1.0-SNAPSHOT
```
3. Create a Kubernetes generic secret with administrator username and credentials
```console
KC_ADMIN_PASSWORD=<enter your password>

kubectl create secret generic keycloak-admin-secret --from-literal=username=admin --from-literal=password=$KC_ADMIN_PASSWORD 
```
4. Install Keycloak Helm Chart with custom values.
```console
helm install test ./edge-iam-keycloak/keycloak-server/helm/ -f ./edge-iam-keycloak/keycloak-server/helm/example/values_kube_plain_development.yaml
```

# Deploy Keycloak for development with Keycloak profile prod activated
## Steps
1. Create a Kind cluster.
```console
cat <<EOF | kind create cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  kubeadmConfigPatches:
  - |
    kind: InitConfiguration
    nodeRegistration:
      kubeletExtraArgs:
        node-labels: "ingress-ready=true"
  extraPortMappings:
  - containerPort: 80
    hostPort: 80
    protocol: TCP
  - containerPort: 443
    hostPort: 443
    protocol: TCP
EOF
```
2. Setting up a Nginx Ingress Controller.
```console
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```
3. (Optional) Load Keycloak local docker image.
```console
kind load docker-image navify-registry.jfrog.io/edge-platform/edge-iam-keycloak:0.1.0-SNAPSHOT
```
4. Create a Kubernetes generic secret with database user password.
```console
POSTGRESQL_USER_PASSWORD=`cat /dev/urandom | tr -dc A-Za-z0-9 | head -c 20` &&
kubectl create secret generic postgres-secrets --from-literal=password=$POSTGRESQL_USER_PASSWORD
```    
5. Install PostgreSQL using Helm Chart.
```console
helm install kc-db-test oci://registry-1.docker.io/bitnamicharts/postgresql --wait \
    --set   auth.database=keycloak \
    --set   auth.enablePostgresUser=false \
    --set   auth.username=kc_user \
    --set   auth.existingSecret=postgres-secrets \
    --set   auth.secretKeys.userPasswordKey=password \
    --set   primary.persistence.enabled=true \
    --set   primary.persistence.storageClass=standard \
    --set   primary.persistence.size=5Gi
```
6. Create a Kubernetes generic secret with database username and password.
```console
kubectl create secret generic keycloak-db-secret --from-literal=username=kc_user --from-literal=password=$POSTGRESQL_USER_PASSWORD
```
7. Create a Kubernetes generic secret with Keycloak administrator username and password.
```console
KC_ADMIN_PASSWORD=<enter your password> &&
kubectl create secret generic keycloak-admin-secret --from-literal=username=admin --from-literal=password=$KC_ADMIN_PASSWORD
```
8. Create a certificate to enable TLS in Keycloak containers and Kubernetes TLS secret.
```console
openssl req -subj '/CN=test-edge-iam-keycloak-server.default.svc.cluster.local/O=Test/C=US' -newkey rsa:2048 -nodes -keyout /tmp/kc_key.pem -x509 -days 365 -out /tmp/kc_certificate.pem &&
kubectl create secret tls keycloak-tls --cert /tmp/kc_certificate.pem --key /tmp/kc_key.pem
```
9. Create a certificate to enable Ingress TLS for Keycloak and its Kubernetes TLS secret.
```console
openssl req -subj '/CN=keycloak.kind.local/O=Test/C=US' -newkey rsa:2048 -nodes -keyout /tmp/kc_ingress_key.pem -x509 -days 365 -out /tmp/kc_ingress_certificate.pem &&
kubectl create secret tls keycloak-ingress-tls --cert /tmp/kc_ingress_certificate.pem --key /tmp/kc_ingress_key.pem
```
10. Edit hosts file and include the following line:
```console
127.0.0.1 keycloak.kind.local
```
11. Install Keycloak Helm Chart with custom values.
```console
helm install test ./edge-iam-keycloak/keycloak-server/helm/ -f ./edge-iam-keycloak/keycloak-server/helm/example/values_kube_plain_development_with_kc_profile_prod_activated.yaml
```

# Install Keycloak in productive mode

## Prerequisites

- [navify Anywhere Kubernetes cluster](https://v2.docs.platform.navify.com/docs-edge-infrastructure/docs/getting-started/getting-started).

## Steps

1. Install PostgreSQL.
2. Create a Kubernetes generic secret with database username and password.
```console
POSTGRESQL_USER_PASSWORD=<enter your postgresql user password> &&
kubectl create secret generic keycloak-db-secret --from-literal=username=kc_user --from-literal=password=$POSTGRESQL_USER_PASSWORD
```
3. Create a Kubernetes generic secret with Keycloak administrator username and password.
```console
KC_ADMIN_PASSWORD=<enter your password> &&
kubectl create secret generic keycloak-admin-secret --from-literal=username=admin --from-literal=password=$KC_ADMIN_PASSWORD
```
4. Install Keycloak Helm Chart with custom values.
```console
helm install test ./edge-iam-keycloak/keycloak-server/helm/ -f ./edge-iam-keycloak/keycloak-server/helm/example/values_kube_na_production_configuration.yaml
```