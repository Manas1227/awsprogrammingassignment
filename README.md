# Car Recognition and Store key in SQS
I created this repository for demonstrate AWS Programming Assignment for course CS-643

Main purposr of the repository is to make the development on VS code and deployment of the code to EC2 instance much easier.
This repository contains the code for Car recognition portion of the Programmin Assignment which includes
- Fetching images from s3 bucket
- Identifying images that is it car or not
- Store all the car image Keys on SQS which is an entry point for the next instance B


# Steps to create EC2 instance
Go to the EC2 service on AWS console
There is an option 'Launch Instances' to create new instance
After click on the button it will going to ask some informations to create the instance
- Enter Name of the instance
- Choose an OS image for instance which is known as Amazon Instance Imange (AMI)
- Select Architecture of the instance
- Now it will ask to choose the Instance type as per the requirements but for this assignment we can go with the Micro instance
- Select the appropiate Key pair credentials which help to connect with the instance
- There is an option for security to control the traffic for the instance, and for this assignment we allow all the SSH, HTTP, and HTTPS trafic from the internet.
- After selecting all the additional details we can finally click on Launch Instance to start the instance.

Once the instance is moved to running state we can connect to the newly launched instance to deploy our program code on the instance.

# Steps to add credentials which allow to access different services 
Navigate to home directory
``` bash
cd ~    
```
Check it .aws already exists
``` bash
ls -a   
```
create .aws directory
``` bash
mkdir .aws     
```
Navigate to .aws directory
``` bash
cd ~/.aws      
```
If file doesn't exists create it otherwise edit the file
``` bash
touch credentails     
```
Past your access key, secret key, and session token
``` bash
nano credentails      
```
To ensure that it set up correctly
``` bash
aws sts get-caller-identity     
```

# Step to perform on EC2 instance to sucessfully deploy, compile, and run the code
Update all the system level packages
```bash
sudo yum update -y
```

To install and confirm the installation of maven on the EC2 instance
```bash
sudo yum install maven -y
sudo mvn -version
```

To install and verify the installation of appropiate Java version on EC2 instance
```bash
sudo yum install java-17-amazon-corretto-devel -y
```

To install git on the EC2 instance
```bash
sudo yum install git 
```

To clone the github remote repository to the EC2 instance
```bash
git clone https://github.com/<user_name>/<repository>
```

After sucessfully clone the repository change current directory to the root of newly cloned project
```bash
cd awsprogrammingassignment
```

Clean previous compiled file, compile source code and create a JAR file in the target directory
```bash
mvn clean install
```

To execute the maven project
```bash
mvn exec:java -Dexec.mainclass="com.example.<main_class_name>"
```

# NOTE
Whenever you restart the lab to work on your existing project on EC2 instances, it's important to update the AWS credentials file with the new information, such as the secret key, access key, and session token. Below are the steps on how to do it:

- After connecting to the specific EC2 instance, navigate to the /home/ec2-user directory.
- Execute the following commands one by one to ensure that the .aws directory exists and contains the credentials file. These commands also help in updating old or expired credentials with the new ones:

```sh
ls -a
cd ~/.aws
ls
nano credentials
```

In the nano editor, use `ctrl + s` to save the changes and `ctrl + x` to exit the editor.