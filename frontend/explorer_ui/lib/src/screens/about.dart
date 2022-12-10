import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

// About page.
// - Logo
// - explanation
class About extends ConsumerWidget {
  // Constructor.
  const About({super.key});

  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // A widget that centers its child within itself.
    return Center(
        // A widget that displays its children in a vertical array.
        child: Column(
      // X and Y alignments.
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // Royllo icon.
        Image.asset('assets/images/logo_large_with_text.png'),
        // Project description
      ],
    ));
  }
}
