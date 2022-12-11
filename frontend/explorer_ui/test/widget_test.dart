// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:explorer_ui/main.dart';

void main() {
  testWidgets('About page exists', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const RoylloExplorerUI());

    // Go to 'About' page.
    await tester.tap(find.byIcon(Icons.help));
    await tester.pump();

    // Verify that the text is here.
    expect(find.text('About Royllo...'), findsOneWidget);
  });
}
