# Keycloak - Config as Code Demo
**Author:** Maik Kingma
- Blog - [the/experts.](https://blog.the-experts.nl/maikkingma)
- LinkedIn - [Maik Kingma](https://www.linkedin.com/in/maik-kingma)
- Twitter - [@MaikKingma](https://www.twitter.com/maikkingma)

In case that you are in need of some general information on Keycloak and how different building blocks are used within
it, feel free to read my [introductory blog article](https://blog.the-experts.nl/maikkingma/identity-and-access-management-iam-with-keycloak-53g1)
on _Identity and Access Management (IAM) with Keycloak_.


## Table of contents
<!-- TOC -->
* [Keycloak - Config as Code Demo](#keycloak---config-as-code-demo)
  * [Table of contents](#table-of-contents)
  * [Prerequisites](#prerequisites)
  * [Creating the Maven Project](#creating-the-maven-project)
    * [The directory structure](#the-directory-structure)
    * [Cleanup of generated files](#cleanup-of-generated-files)
    * [Create the parent pom.xml file](#create-the-parent-pomxml-file)
    * [Updating the child pom.xml files](#updating-the-child-pomxml-files)
    * [Package our maven project](#package-our-maven-project)
  * [The Keycloak Distribution](#the-keycloak-distribution)
  * [Updating the Keycloak submodule](#updating-the-keycloak-submodule)
  * [Docker setup](#docker-setup)
  * [Launching Keycloak](#launching-keycloak)
<!-- TOC -->

## Prerequisites
* Java 17
* Docker Desktop
* Maven

## Creating the Maven Project
(blog article: TODO place link here)

In this section we will create the base directory structure and initialize the maven project.
### The directory structure
Create your root directory with and switch the terminal location into it by running
```shell
mkdir keycloak-configAsCode-demo && cd keycloak-configAsCode-demo
```
We now create two maven artifacts called keycloak and java-configuration by running the following two commands in our terminal window
```shell
mvn archetype:generate -DgroupId=nl.the_experts.keycloak -DartifactId=keycloak -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
mvn archetype:generate -DgroupId=nl.the_experts.keycloak -DartifactId=java-configuration -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```
### Cleanup of generated files
Remove the generated files ``java-configuration/src/main/java/nl/theexperts/keycloak/App.java`` and ``keycloak/src/main/java/nl/theexperts/keycloak/App.java`` and the corresponding test files.

### Create the parent pom.xml file
In the root directory of the project create a file named ``pom.xml`` with the following content:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

    <groupId>nl.the_experts.keycloak</groupId>
    <artifactId>keycloak-configascode-demo</artifactId>
    <version>0.0.1-local</version>
    <packaging>pom</packaging>

    <modules>
        <module>keycloak</module>
        <module>java-configuration</module>
    </modules>

    <properties>
        <!-- Java -->
        <java.version>17</java.version>

        <!-- Maven -->
        <maven-surefire-plugin.version>3.0.0-M5</maven-surefire-plugin.version>
        <maven.shade.plugin.version>3.3.0</maven.shade.plugin.version>
        <maven.jar.plugin.version>3.2.2</maven.jar.plugin.version>

        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <maven.build.timestamp.format>yyyy-MM-dd'T'HH:mm:ss'Z'</maven.build.timestamp.format>

        <!-- Project specific -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

        <!-- Keycloak -->
        <keycloak.version>22.0.5</keycloak.version>
        <quarkus.version>2.13.3.Final</quarkus.version>
        <quarkus.resteasy.version>2.13.5.Final</quarkus.resteasy.version>
        <quarkus.native.builder-image>mutable-jar</quarkus.native.builder-image>

        <!-- Plugins -->
        <build-helper-maven-plugin.version>3.3.0</build-helper-maven-plugin.version>
    </properties>


    <dependencyManagement>
        <dependencies>
            <!--Quarkus-->
            <dependency>
                <groupId>io.quarkus.arc</groupId>
                <artifactId>arc</artifactId>
                <version>${quarkus.version}</version>
            </dependency>
            <!--keycloak-->
            <dependency>
                <groupId>org.keycloak</groupId>
                <artifactId>keycloak-quarkus-server</artifactId>
                <version>${keycloak.version}</version>
            </dependency>
            <dependency>
                <groupId>org.keycloak</groupId>
                <artifactId>keycloak-quarkus-dist</artifactId>
                <version>${keycloak.version}</version>
                <type>zip</type>
            </dependency>
            <dependency>
                <groupId>org.keycloak</groupId>
                <artifactId>keycloak-admin-client</artifactId>
                <version>${keycloak.version}</version>
            </dependency>
            <dependency>
                <groupId>org.keycloak</groupId>
                <artifactId>keycloak-core</artifactId>
                <version>${keycloak.version}</version>
            </dependency>
            <dependency>
                <groupId>org.keycloak</groupId>
                <artifactId>keycloak-services</artifactId>
                <version>${keycloak.version}</version>
            </dependency>

            <!--REST-->
            <dependency>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-resteasy-jackson</artifactId>
                <version>${quarkus.resteasy.version}</version>
            </dependency>
            <dependency>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-rest-client</artifactId>
                <version>${quarkus.version}</version>
            </dependency>
            <dependency>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-rest-client-jackson</artifactId>
                <version>${quarkus.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>${maven-surefire-plugin.version}</version>
                    <configuration>
                        <excludes>
                            <exclude>**/*IntegrationTest.java</exclude>
                        </excludes>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-dependency-plugin</artifactId>
                    <executions>
                        <execution>
                            <id>unpack-keycloak-server-distribution</id>
                            <phase>package</phase>
                            <goals>
                                <goal>unpack</goal>
                            </goals>
                            <configuration>
                                <artifactItems>
                                    <artifactItem>
                                        <groupId>org.keycloak</groupId>
                                        <artifactId>keycloak-quarkus-dist</artifactId>
                                        <type>zip</type>
                                        <outputDirectory>target</outputDirectory>
                                    </artifactItem>
                                </artifactItems>
                                <excludes>**/lib/**</excludes>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
                <plugin>
                    <groupId>io.quarkus</groupId>
                    <artifactId>quarkus-maven-plugin</artifactId>
                    <version>${quarkus.version}</version>
                    <configuration>
                        <finalName>keycloak</finalName>
                        <buildDir>${project.build.directory}/keycloak-${keycloak.version}</buildDir>
                    </configuration>
                    <executions>
                        <execution>
                            <goals>
                                <goal>build</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```
Let's briefly analyze the different sections we have now added to our root parent POM. In preparation for our base Keycloak setup the most basic blocks are already in there. We will use those in Pt.2 of this blog series.
- In the ``modules`` element we register our two submodules we previously created.
- In the ``properties`` element we maintain all the (version) variables that we will use throughout our project.
- In the ``dependencyManagement`` element, all dependencies and their versions we want to use should be added.
- Contained in the ``build`` section you will find the ``pluginManagement`` where we define the plugins that we will use later on throughout our project.

### Updating the child pom.xml files
All that remains is to update the child POMs and we are ready to go. Please replace the content of ``./keycloak/pom.xml`` with the following snippet:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>nl.the_experts.keycloak</groupId>
    <artifactId>keycloak-configascode-demo</artifactId>
    <version>0.0.1-local</version>
  </parent>
  <artifactId>keycloak</artifactId>

</project>
```
and accordingly, ``./java-configuration/pom.xml`` with this snippet:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>nl.the_experts.keycloak</groupId>
    <artifactId>keycloak-configascode-demo</artifactId>
    <version>0.0.1-local</version>
  </parent>
  <artifactId>java-configuration</artifactId>
</project>
```

### Package our maven project
You should now be able to run package the parent maven project by running 
```shell
mvn clean package
```
For now, the project and its submodules are completely empty. We will now first focus on setting up a clean and basic Keycloak distribution that
we can run within a container platform such as Docker.

## The Keycloak Distribution
Once again, there are multiple ways to get started. We could make use of the Keycloak docker base image, provided by Red Hat. However, that would leave us less flexible when it comes to fixing security issues (reported CVEs) in the dependency tree of Keycloak itself. Inspired by a blog post by Thomas Darimont and Sebastian Rose, two colleagues from Germany, on [Keycloak.X, but secure – without vulnerable libraries](https://blog.codecentric.de/keycloak-x-but-secure-without-vulnerable-libraries), we are not going to use this base image. Instead, we add the Keycloak distribution as a dependency to our project. If you are interested in the benefits of doing so, feel free to read their interesting blog. In summary,
> a custom distribution can support in the following:
>
> - Use of an optimized configuration for fast server start-up
> - Support of own extensions and themes
> - Only actually used Quarkus extensions activated
> - Additionally needed Quarkus extensions are supported
> - Libraries can be upgraded to a current patch level.
>
> (Quote from [Keycloak.X, but secure – without vulnerable libraries](https://blog.codecentric.de/keycloak-x-but-secure-without-vulnerable-libraries))

## Updating the Keycloak submodule
We start by updating the file `keycloak/pom.xml` with the following content:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>nl.the_experts.keycloak</groupId>
        <artifactId>keycloak-configascode-demo</artifactId>
        <version>0.0.1-local</version>
    </parent>
    <artifactId>keycloak</artifactId>
    <dependencies>
        <dependency>
            <!-- Keycloak Distribution -->
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-quarkus-dist</artifactId>
            <type>zip</type>
        </dependency>
        <dependency>
            <!-- Keycloak Quarkus Server Libraries-->
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-quarkus-server</artifactId>
        </dependency>
    </dependencies>
    
    <build>
        <finalName>keycloak-${project.version}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
What we added is the `dependencies` element with in it the Keycloak distribution and the Keycloak Quarkus server. Notice that the distribution is in zip format. We handle this by having added a reference to the root POM's `maven-dependency-plugin` in the `build` section, which contains an `execution`to unpack the zip file into our target directory.
If we now run ``mvn clean package``, our target directory in the keycloak submodule should look something like this:

![Target Directory Content](https://ccnl-blog.s3.amazonaws.com/uploads/articles/ai96lh6cdf91za6b7nht.png)

## Docker setup
As previously announced, we will use Docker to containerize our solution. In doing so, we create a portable solution that can easily be started on any OS that has Java, Maven and Docker Desktop installed.

We will add two files to the root of our project. Firstly,
``./Dockerfile`` with content:

```dockerfile
FROM registry.access.redhat.com/ubi8-minimal:8.7 AS builder
RUN microdnf update -y && \
    microdnf install -y java-17-openjdk-headless && microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:1000:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd

COPY --chown=keycloak:keycloak keycloak/target/keycloak-22.0.5  /opt/keycloak

USER 1000

RUN /opt/keycloak/bin/kc.sh build --db=postgres

FROM registry.access.redhat.com/ubi8-minimal:8.7

RUN microdnf update -y && \
    microdnf reinstall -y tzdata && \
    microdnf install -y java-17-openjdk-headless && \
    microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:10001:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd && \
    ln -sf /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime # set timezone

COPY --from=builder --chown=1000:0 /opt/keycloak /opt/keycloak

USER 1000
WORKDIR /opt/keycloak-config

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start"]
```
One may notice the use of a builder in our ``Dockerfile``. Since the introduction and support of multistage builds in [Docker 17.06 CE](https://www.docker.com/blog/multi-stage-builds/), the builder pattern makes use of two Docker images, one to create a base image for building assets and subsequently a second image to run the assets.

Since we added the Keycloak Distribution as a dependency, we are free to pick a base image of our choosing. The Universal Base Image Minimal [ubi8-minimal:8.7](https://catalog.redhat.com/software/containers/ubi8/ubi-minimal/5c359a62bed8bd75a2c3fba8) by Red Hat is a stripped down image that uses microdnf as a package manager. Furthermore, JDK 17 is installed, required user permissions set, and eventually we copy our packaged Keycloak distribution into the builder image.
In the runner image section, after initialising the base image with required libraries, we only copy the ``/opt/keycloak`` directory from the builder into the runner image and expose ports 8080 and 8443. Last but not least, the default entrypoint for our runner image is defined.

Secondly, we need to add the file ``./docker-compose.yml`` with content:

```yml
version: '3.9'

services:
  postgres:
    image: postgres:14.4
    container_name: postgres
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_PASSWORD: postgres
      POSTGRES_USER: postgres
    ports:
      - '5432:5432'
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -d $${POSTGRES_DB} -U $${POSTGRES_USER}"]
      interval: 10s
      timeout: 5s
      retries: 5

  keycloak:
    build: .
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      - KC_LOG_LEVEL=info
      - KEYCLOAK_ADMIN=admin
      - KEYCLOAK_ADMIN_PASSWORD=admin
      - KC_HOSTNAME_STRICT_HTTPS=false
      - KC_DB=postgres
      - KC_DB_SCHEMA=public
      - KC_DB_URL_PORT=5432
      - KC_DB_URL_HOST=postgres
      - KC_DB_USERNAME=postgres
      - KC_DB_PASSWORD=postgres
      - KC_HEALTH_ENABLED=true
      - KC_METRICS_ENABLED=false
    container_name: keycloak
    entrypoint: /opt/keycloak/bin/kc.sh start-dev --http-enabled=true --cache=local
    ports:
      - "8080:8080"
    healthcheck:
      test: [ "CMD-SHELL", "wget -q -O /dev/null http://keycloak:8080/" ]
      interval: 10s
      timeout: 10s
      retries: 10
      start_period: 10s
```
Fairly straight forward, this file allows us to compose our required containers in our local environment.
The first listed service is a simple Postgres DB. Once this service becomes healthy (health check defined) our second service Keycloak, which depends on the availability of our DB, will be built and launched.

## Launching Keycloak
As required by our self-defined acceptance criteria, we can now launch our Keycloak instance by running the following commands from the root directory of our project:
```bash
mvn clean package && docker compose up
```







