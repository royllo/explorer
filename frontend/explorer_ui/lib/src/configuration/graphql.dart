import 'package:gql_http_link/gql_http_link.dart';
import 'package:ferry/ferry.dart';

// A simple HttpLink.
// TODO Set this as a parameter.
final link = HttpLink("http://localhost:9090/graphql");

// GraphQL Client.
final client = Client(link: link);