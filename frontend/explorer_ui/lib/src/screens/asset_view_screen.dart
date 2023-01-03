import 'package:explorer_ui/src/providers/asset_get_provider.dart';
import 'package:explorer_ui/src/widgets/default_bottom_navigation_bart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../widgets/default_app_bar.dart';

// Asset page - Displays an asset.
// - Search field
// - Asset information
class AssetViewScreen extends ConsumerWidget {
  // Constructor.
  const AssetViewScreen({Key? key}) : super(key: key);

  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // We watch change in asset id
    final result = ref.watch(callAssetByAssetIdProvider);

    return Scaffold(
      // =====================================================================
      // An app bar consists of a toolbar and potentially other widgets.
      appBar: defaultRoylloAppBar(context),

      // =====================================================================
      // The page content.
      // A widget that centers its child within itself
      body: Center(
          // A widget that displays its children in a vertical array.
          child: Column(
            // X and Y alignments.
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // =====================================================================================================================
              // Royllo icon.
              Image.asset('assets/images/logo_large_without_text.png'),

              // =====================================================================================================================
              // Display the asset
              result.when(
                loading: () => const CircularProgressIndicator(),
                data: (result) {
                  var asset = result.data?.assetByAssetId;
                  if (asset == null) {
                    return Text("No asset found with this asset id");
                  } else {
                    return Text("Asset found : '${asset.assetId} / ${asset.name}'");
                  }
                },
                error: (err, stack) => Text('An error occurred: $err'),
              ),
          // =====================================================================================================================
        ],
      )),

      // =====================================================================
      // Bottom navigation bar.
      bottomNavigationBar: defaultRoylloBottomNavigationBar(context),
    );
  }
}
