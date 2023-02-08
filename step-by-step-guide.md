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
* [Prerequisites](#prerequisites)
* [Creating the Maven Project](#creating-the-maven-project)
  * [The directory structure](#the-directory-structure)
  * [Cleanup](#cleanup)
  * [Create the parent pom.xml file](#create-the-parent-pomxml-file)
  * [Updating the child pom.xml files](#updating-the-child-pomxml-files)
  * [Package our maven project](#package-our-maven-project)
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
        <keycloak.version>20.0.3</keycloak.version>
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

Stay tuned for more!








