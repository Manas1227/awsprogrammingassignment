# awsprogrammingassignment
I created this repository for demonstrate AWS Programming Assignment for course CS-643

# Step to perform on EC2 instance to sucessfully compile and run the code
```bash
sudo yum update -y
sudo yum install maven -y
sudo yum install java-17-amazon-corretto-devel -y
sudo yum install git 
git clone https://github.com/Manas1227/awsprogrammingassignment
cd awsprogrammingassignment
mvn clean package    
```
clean previous compiled file, compile source code and create a JAR file in the target directory


# Steps to add credentials
``` bash
cd ~    
```
Navigate to home directory
``` bash
ls -a   
```
Check it .aws already exists
``` bash
mkdir .aws     
```
create .aws directory
``` bash
cd ~/.aws      
```
Navigate to .aws directory
``` bash
touch credentails     
```
If file doesn't exists create it otherwise edit the file
``` bash
nano credentails      
```
Past your access key, secret key, and session token
``` bash
aws sts get-caller-identity     
```
To ensure that it set up correctly
