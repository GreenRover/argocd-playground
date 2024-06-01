oc login -u kubeadmin https://api.crc.testing:6443
oc project sttrs-argocd

helm upgrade app-of-apps -n sttrs-argocd --install ./argocd
./localdev-scripts/deploy-to-local-openshift.bash

oc apply -f ./argocd-sttrs.yaml
token=`oc create token localdev -n demo --duration=24h`

oc project demo
echo "ENV for operator:"
echo "OC_URL=https://api.crc.testing:6443"
echo "OC_NAME_SPACE=demo"
echo "OC_AUTH_TOKEN=$token"