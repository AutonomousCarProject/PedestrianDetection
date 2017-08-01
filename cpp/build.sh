gcc -shared -o libFlyCamera.so -fPIC FlyCamera.cpp -L ./ -lflycapture -lflycapture-c -I . -I /usr/java/jdk1.8.0_91/include/ -I /usr/java/jdk1.8.0_91/include/linux/
echo "Generated..."
# cp libFlyCamera.so /home/ssuri/work/java/OtherCode/CarProject/libs/
# echo "Copied..."
echo "Done!"
