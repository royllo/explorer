import 'package:explorer_ui/graphql/api/getUser.data.gql.dart';
import 'package:explorer_ui/graphql/api/getUser.req.gql.dart';
import 'package:explorer_ui/graphql/api/getUser.var.gql.dart';
import 'package:ferry/ferry.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:gql_http_link/gql_http_link.dart';

void main() {
  test('About page exists', () async {
    // // Build our app and trigger a frame.
    // await tester.pumpWidget(const ProviderScope(child: RoylloExplorerUI()));
    //
    // // Go to 'About' page.
    // await tester.tap(find.byIcon(Icons.help));
    // await tester.pump();

    // Verify that the text is here.
    // expect(find.text('About Royllo...'), findsOneWidget);

    // GraphQL Client.
    final link = HttpLink("http://localhost:9090/graphql");
    final client = Client(link: link);

    final request = GuserByUsernameReq((b) => b
      //..fetchPolicy = FetchPolicy.NetworkOnly
      ..vars.username = "anonymous");

    final OperationResponse<GuserByUsernameData, GuserByUsernameVars> event = await client.request(request).first;
    // print("Error ${event.graphqlErrors}");
    print(event.linkException);
    print("User is ${event.data?.userByUsername?.id} / ${event.data?.userByUsername?.username}");
  });
}
