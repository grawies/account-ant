echo creating manifest.txt
printf "Manifest-Version: 1.0\n" > manifest.txt
printf "Class-Path: .\n" >> manifest.txt
printf "Main-Class: view.Main\n" >> manifest.txt
echo creating jar-file
cd bin
jar cfm Booking.jar ../manifest.txt ./*/*.class
cd ..
echo moving stuff
mv bin/Booking.jar jar/
mv manifest.txt jar/
echo executing jar program\!
java -jar jar/Booking.jar

