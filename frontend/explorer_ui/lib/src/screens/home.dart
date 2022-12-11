import 'package:explorer_ui/src/widgets/default_app_bar.dart';
import 'package:explorer_ui/src/widgets/default_bottom_navigation_bart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

// Project homepage.
// - Logo
// - Search field
class HomeScreen extends ConsumerWidget {
  // Constructor.
  const HomeScreen({super.key});

  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
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
            Image.asset('assets/images/logo_homepage.png'),
            // A widget that sizes its child to a fraction of the total available space.
            const FractionallySizedBox(
              widthFactor: 0.7,
              // The search field
              child: TextField(
                autofocus: true,
                // Set the radius.
                decoration: InputDecoration(
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(
                    Radius.circular(20),
                  )),
                  // Set a search icon as prefix.
                  prefixIcon: Icon(Icons.search),
                  // Hint text to help the user.
                  hintText: 'Type an asset id or an asset name',
                ),
              ),
            )
          ],
        )),

        // =====================================================================
        // Bottom navigation bar.
        bottomNavigationBar: defaultRoylloBottomNavigationBar(context));
  }
}
