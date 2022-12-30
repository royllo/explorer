import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/widgets/asset_search_form.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('About screen', (tester) async {
    // We load the homepage
    await tester.pumpWidget(const ProviderScope(
      child: RoylloExplorerUI(),
    ));

    // On the home screen, we click on the about button
    await tester.tap(find.byIcon(Icons.help).first);
    await tester.pump();
    await tester.pumpAndSettle();

    // We check that we arrived on the about page where the about text is
    expect(find.text('About Royllo.'), findsOneWidget);

    // We check the buttons.
    expect(find.byIcon(Icons.home), findsNWidgets(2));
    expect(find.byIcon(Icons.help), findsOneWidget);

    // We go back home
    await tester.tap(find.byIcon(Icons.home).first);
    await tester.pump();
    await tester.pumpAndSettle();

    // We check that we are back on the home page
    expect(find.image(const AssetImage('assets/images/logo_homepage.png')), findsOneWidget);
    expect(find.byType(AssetSearchForm), findsOneWidget);
  });
}