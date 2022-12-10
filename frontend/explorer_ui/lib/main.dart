import 'package:explorer_ui/src/screens/home.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

// This function tells Dart where the program starts, and it must be in the file that is considered the "entry point" for you program.
void main() {
  runApp(
    // For widgets to be able to read providers, we need to wrap the entire application in a "ProviderScope" widget.
    // This is where the state of our providers will be stored.
    // A provider is an object that encapsulates a piece of state and allows listening to that state.
    const ProviderScope(
      child: RoylloExplorerUI(),
    ),
  );
}

// My main class, its launched by main().
// A ConsumerWidget is a StatelessWidget that can listen to providers.
class RoylloExplorerUI extends ConsumerWidget {
  // This is a dart constant constructor - It involve instance variables that cannot be changed.
  // Using a const constructor allows a class of objects that cannot be defined using a literal syntax to be assigned to a constant identifier.
  // When using the const keyword for initialization, no matter how many times you instantiate an object with the same values, only one instance exists in memory.
  const RoylloExplorerUI({super.key});

  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // MaterialApp is a widget that wraps a number of widgets that are commonly required for Material Design applications.
    return MaterialApp(
      // Scaffold implements the basic Material Design visual layout structure.
      home: Scaffold(
        // An app bar consists of a toolbar and potentially other widgets, such as a TabBar and a FlexibleSpaceBar.
        appBar: AppBar(title: const Text('Royllo')),
        // Content.
        body: const Home(),
        // Bottom navigation bar.
        bottomNavigationBar: const Text('Copyright Royllo'),
      ),
      // Defines the configuration of the overall visual Theme for a MaterialApp or a widget subtree within the app.
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
