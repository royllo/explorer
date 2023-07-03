# Build
install:
    mvn clean install

install_no_test:
    mvn clean install -DskipTests=true

# Run
run_web_server_backend:
    mvn spring-boot:run -Dspring-boot.run.profiles=dev -f backend/servers/explorer-web/pom.xml

run_web_server_frontend:
    cd backend/servers/explorer-web
    npm run build && npm run watch

# Release
prepare_release:
    git remote set-url origin git@github.com:royllo/explorer.git
    git checkout development
    git pull
    git status
    mvn gitflow:release-start

finish_release:
    mvn gitflow:release-finish

# Utils
download_dependencies:
    mvn dependency:sources dependency:resolve -Dclassifier=javadoc
