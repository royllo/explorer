import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/widgets/asset_search_form.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Home screen', (tester) async {
    // We load the homepage
    await tester.pumpWidget(const ProviderScope(
      child: RoylloExplorerUI(),
    ));

    // Check that the logo is there
    final logoImage = find.image(const AssetImage('assets/images/logo_homepage.png'));
    expect(logoImage, findsOneWidget);

    // Check that the search form is here
    final searchForm = find.byType(AssetSearchForm);
    expect(searchForm, findsOneWidget);

    // We check the buttons.
    expect(find.byIcon(Icons.home), findsNWidgets(2));
    expect(find.byIcon(Icons.help), findsOneWidget);
  });
}
