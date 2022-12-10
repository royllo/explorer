import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

// Project homepage.
// - Logo
// - Search field
class Home extends ConsumerWidget {
  // Constructor.
  const Home({super.key});

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
        Image.asset('assets/images/royllo_logo_homepage.png'),
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
                  )
              ),
              // Set a search icon as prefix.
              prefixIcon: Icon(Icons.search),
              // Hint text to help the user.
              hintText: 'Type an asset id or an asset name',
            ),
          ),
        )
      ],
    ));
  }
}
