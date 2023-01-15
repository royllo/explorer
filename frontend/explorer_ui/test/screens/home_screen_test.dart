import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/widgets/asset_search_form.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Home screen tests', (tester) async {
    // We load the homepage
    await tester.pumpWidget(const ProviderScope(
      child: RoylloExplorerUI(),
    ));

    // Check that the logo and the search form are there
    expect(find.image(const AssetImage('assets/images/logo_homepage.png')), findsOneWidget);
    expect(find.byType(AssetSearchForm), findsOneWidget);

    // We check the buttons.
    expect(find.byIcon(Icons.home), findsNWidgets(2));
    expect(find.byIcon(Icons.help), findsOneWidget);
  });
}