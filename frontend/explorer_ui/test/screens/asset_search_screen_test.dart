import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart';
import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/widgets/asset_search_form.dart';
import 'package:ferry/ferry.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/annotations.dart';

import 'asset_search_screen_test.mocks.dart';

@GenerateNiceMocks([
  MockSpec<OperationResponse<GqueryAssetsData, GqueryAssetsVars>>(as: #MockResponse) // Generate typed OperationResponse as MockResponse class
])
void main() {
  testWidgets('Asset screen tests', (tester) async {

    final mockResponse = MockResponse();
    // GqueryAssetsData.fromJson(json)

    // We load the homepage
    await tester.pumpWidget(const ProviderScope(
      child: RoylloExplorerUI(),
    ));

    // Check that the logo and the search form are there
    expect(find.image(const AssetImage('assets/images/logo_homepage.png')), findsOneWidget);
    expect(find.byType(AssetSearchForm), findsOneWidget);
  });
}
