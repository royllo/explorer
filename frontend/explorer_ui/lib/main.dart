import 'package:explorer_ui/src/configuration/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_web_plugins/url_strategy.dart';

// Providers are declared globally and specify how to create a state.

// This provider keep the current value in the search field.
final searchFieldValueProvider = StateProvider<String>((ref) => "");
// This provider keeps the search send to the API.
final searchRequestProvider = StateProvider<SearchRequest>((ref) => SearchRequest(""));

// This class defines what we search.
class SearchRequest {
  String query;
  int pageNumber;

  // Constructor (Page number is optional).
  SearchRequest(this.query, {this.pageNumber = 1});
}

// This function tells Dart where the program starts, and it must be in the file that is considered the "entry point" for you program.
void main() {
  // By default, with flutter, paths are read and written with an hash. With this method, paths are read and written without a hash.
  usePathUrlStrategy();

  //  runApp inflates the given widget and attach it to the screen.
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
    return MaterialApp.router(
      routerConfig: roylloRouter,
    );
  }
}


