import 'package:explorer_ui/src/widgets/default_bottom_navigation_bart.dart';
import 'package:flutter/material.dart';

import '../widgets/default_app_bar.dart';

// Asset page - Displays an asset.
// - Search field
// - Asset information
class AssetViewScreen extends StatelessWidget {
  const AssetViewScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
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
          // Royllo icon.
          Image.asset('assets/images/logo_large_without_text.png'),
          const Text("Asset"),
        ],
      )),

      // =====================================================================
      // Bottom navigation bar.
      bottomNavigationBar: defaultRoylloBottomNavigationBar(context),
    );
  }
}
