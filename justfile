# ======================================================================================================================
# Build
test:
    mvn clean install

integration:
    mvn clean install -P integration

install:
    mvn clean install -DskipTests

build_docker:
    mvn clean install -P release -DskipTests
    mvn spring-boot:build-image -P release -DskipTests -f backend/servers/explorer-batch/pom.xml
    mvn spring-boot:build-image -P release -DskipTests -f backend/servers/explorer-api/pom.xml
    mvn spring-boot:build-image -P release -DskipTests -f backend/servers/explorer-web/pom.xml

# ======================================================================================================================
# Run
run_api:
    mvn spring-boot:run -Dspring-boot.run.profiles=dev -f backend/servers/explorer-api/pom.xml

run_web_backend:
    mvn spring-boot:run -Dspring-boot.run.profiles=dev -f backend/servers/explorer-web/pom.xml

run_web_frontend:
    npm run --prefix backend/servers/explorer-web build && npm run --prefix backend/servers/explorer-web watch

# ======================================================================================================================
# Release
start_release:
    git remote set-url origin git@github.com:royllo/explorer.git
    git checkout development
    git pull
    git status
    mvn gitflow:release-start

finish_release:
    mvn gitflow:release-finish

# ======================================================================================================================
# Docker
view_batch_server_logs:
    docker-compose logs -f royllo-explorer-batch-server

remove_docker_content:
    docker stop $(docker ps -qa)
    docker rm $(docker ps -qa)
    docker rmi -f $(docker images -qa)
    docker volume rm $(docker volume ls -q)
    docker network rm $(docker network ls -q)

# ======================================================================================================================
# Utils
download_dependencies:
    mvn dependency:sources dependency:resolve -Dclassifier=javadoc

update_ubuntu:
    sudo apt-get update
    sudo apt-get -y upgrade