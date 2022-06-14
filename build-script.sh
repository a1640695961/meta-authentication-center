echo 'Git update...'
git pull

echo 'Maven build...'
mvn clean
mvn package

echo 'Docker build...'

docker stop meta-authority-api

docker rm meta-authority-api

docker image prune -a -f

docker build -t registry.cn-shanghai.aliyuncs.com/meta360/meta-authority-api:latest .

echo 'Docker push...'
#docker push registry.cn-shanghai.aliyuncs.com/meta360/meta-authority-api:latest

echo 'Docker run...'
docker-compose up -d
echo 'Done...'

