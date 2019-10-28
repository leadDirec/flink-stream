mvn clean package -Dmaven.test.skip=true
scp -r target/userportrait-0.1.jar  ubuntu@172.81.216.197:/home/ubuntu
ssh  ubuntu@172.81.216.197
sudo  mv  userportrait-0.1.jar /root
sudo -i && cd /root
scp -r userportrait-0.1.jar 10.1.1.37:/usr/local/service/flink
ssh 10.1.1.37 && cd /usr/local/service/flink
mv ./task/userportrait-0.1.jar  ./task/userportrait-0.1.jar_backup
mv userportrait-0.1.jar task/
su hadoop
flink run -yid application_1569475799008_21835 ./task/userportrait-0.1.jar