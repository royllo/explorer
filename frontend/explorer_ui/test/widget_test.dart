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
  });
}
