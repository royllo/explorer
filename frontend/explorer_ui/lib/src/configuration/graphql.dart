import 'package:gql_http_link/gql_http_link.dart';
import 'package:ferry/ferry.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

// Includes riverpod generated classes.
part 'graphql.g.dart';

// We create a provider, which will store the GraphQL connection.
@riverpod
Future<Client> graphqlClient(GraphqlClientRef ref) async {
  return await gqlClient();
}

// Returns the Graphql client.
Future<Client> gqlClient() async {
  return Client(link: HttpLink(_getGraphQLEndpoint()));
}

// returns the graphql endpoint.
String _getGraphQLEndpoint() {
  return const String.fromEnvironment('GRAPHQL_ENDPOINT',
      defaultValue: 'http://localhost:9090/graphql');
}
