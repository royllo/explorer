import 'package:gql_http_link/gql_http_link.dart';
import 'package:ferry/ferry.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

// Includes riverpod generated classes
part 'graphql.g.dart';

// Some explanations about future:
// Asynchronous operations let your program complete work while waiting for another operation to finish.
// Such asynchronous computations usually provide their result as a Future or, if the result has multiple parts, as a Stream.
// A future represents the result of an asynchronous operation, and can have two states: uncompleted or completed.
// async: You can use the async keyword before a function’s body to mark it as asynchronous.
// await: You can use the await keyword to get the completed result of an asynchronous expression.

/// We create a provider, which will store the GraphQL connection
@riverpod
Future<Client> graphqlClient(GraphqlClientRef ref) async {
  // TODO handle errors in our async function and find a way to display it in the UI
  return await gqlClient();
}

/// Returns the Graphql client
Future<Client> gqlClient() async {
  return Client(link: HttpLink(_getGraphQLEndpoint()));
}

/// Returns the graphql endpoint - Read it from GRAPHQL_API_ENDPOINT
String _getGraphQLEndpoint() {
  return const String.fromEnvironment('GRAPHQL_API_ENDPOINT', defaultValue: 'http://localhost:9090/graphql');
}
