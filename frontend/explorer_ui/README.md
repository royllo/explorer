# Royllo explorer UI

## Command line

- Run tests: ` flutter test`.
- Run app: `flutter run -d chrome`.


## Generate flutter code from graphQL schema

After starting the docker-compose, you can retrieve/update the schema
with : `get-graphql-schema http://localhost:9090/graphql > lib/graphql/schema.graphql`

Then, you can build with : `flutter pub run build_runner build --delete-conflicting-outputs`

## Things I did during development

### Riverpod

Added Riverpod by following this documentation : https://docs-v2.riverpod.dev/docs/getting_started.
A provider is an object that encapsulates a piece of state and allows listening to that state. They are a complete
replacement for patterns like Singletons, Service Locators, Dependency Injection or InheritedWidgets.

### go_router

https://pub.dev/packages/go_router

### Ferry graph

https://ferrygraphql.com/

## Useful commandes

- Run the code-generator with `flutter pub run build_runner watch`