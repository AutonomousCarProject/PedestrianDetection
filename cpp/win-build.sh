x86_64-w64-mingw32-gcc FlyCamera.cpp -shared -o FlyCamera.dll -L ./ -lFlyCapture2 -lFlyCapture2_C -I . -I /usr/java/jdk1.8.0_91/include/ -I /usr/java/jdk1.8.0_91/include/linux/ -fpermissive
echo "Generated..."
echo "Done!"
