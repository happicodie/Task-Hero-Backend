## 1. Install Java 11

1. Open terminal, update by `sudo apt-get update` to the latest version
   
2. Install Java jdk11
   
   `sudo apt install openjdk-11-jdk` and input `Y` to continue

3. Config environment variable:
   
   `vim ~/.bashrc` Append the followings:
   
   ```shell
   #Java config
   export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
   export CLASSPATH=.:${JAVA_HOME}/lib
   export PATH=${JAVA_HOME}/bin:$PATH
   ```

4. Then make it works by command `source ~/.bashrc`

---

## 2. Setup MySQL

### Install MySQL

Install MySQL server and client by command:

`sudo apt-get install mysql-server mysql-client` and input `Y` to continue

### Create and import MySQL database

Then create a database named `task_hero` by following commands:

1. `mysql> CREATE DATABASE task_hero;` to create a database

2. Check if the database was created successfully by entering`SHOW DATABASES;` 

3. `mysql> USE task_hero;` to use the created database

4. `mysql> SET names utf8;` to set database encoding

5. Under MySQL console, source the `data.sql` to import SQL schema

6. Let the `mysql` command could run without `sudo` command and modify password to "root"

   ```sql
   mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
   mysql> FLUSH PRIVILEGES;
   ```

7. Run `mysql> quit` to quit MySQL console

---

## 3. Setup MongoDB

### Install MongoDB

```shell
cat <<"EOF" | bash
VERSION=6.0
sudo apt update && \
sudo apt install -y dirmngr wget gnupg apt-transport-https ca-certificates software-properties-common gnupg && \
wget -qO - https://www.mongodb.org/static/pgp/server-${VERSION}.asc | sudo apt-key add - && \
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/${VERSION} multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-${VERSION}.list && \
sudo apt update && \
sudo apt install -y mongodb-org
EOF
```

### Create MongoDB database

Create a database named `task-hero`:

1. Start MongoDB service by`sudo systemctl enable --now mongod`
2. `mongosh` to enter MongoDB console
3. Run `use task-hero` to create a MongoDB database
4. Run `exit` to quit MongoDB console

----

## 4.  Run TaskHero

Run the backend by

```shell
java -jar task_hero.jar
```

## 5. API Document

### Swagger

http://localhost:8080/swagger-ui/index.html#/