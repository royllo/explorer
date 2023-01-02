import 'package:explorer_ui/src/configuration/routes.dart';
import 'package:explorer_ui/src/providers/asset_search_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

// This provider keep the current value in the search field.
final assetSearchFieldValueProvider = StateProvider<String>((ref) => "");

// Asset search form.
class AssetSearchForm extends ConsumerStatefulWidget {
  // Constructor.
  const AssetSearchForm({super.key});

  @override
  ConsumerState<AssetSearchForm> createState() => _AssetSearchFormState();
}

class _AssetSearchFormState extends ConsumerState<AssetSearchForm> {
  // Create a global key that uniquely identifies the Form widget and allows validation of the form.
  final _formKey = GlobalKey<FormState>();

  // Text editing controller.
  final _controller = TextEditingController();

  // FocusNode can be used by a stateful widget to obtain the keyboard focus and to handle keyboard events.
  // Define the focus node. To manage the lifecycle, create the FocusNode in the initState method, and clean it up in the dispose method.
  late FocusNode _focusNode;

  @override
  void initState() {
    super.initState();
    // When the field is created, we retrieve the value from the provider.
    _controller.text = ref.read(assetSearchFieldValueProvider);
    // Select the text field content when it's created.
    _controller.selection = TextSelection(
      baseOffset: 0,
      extentOffset: _controller.text.length,
    );
    _focusNode = FocusNode();
  }

  @override
  void dispose() {
    // Clean up the focus node when the Form is disposed.
    _focusNode.dispose();
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // A widget that sizes its child to a fraction of the total available space.
    return FractionallySizedBox(
        widthFactor: 0.5,
        // The search field
        child: Form(
          // Build a Form widget using the _formKey created above.
          key: _formKey,
          child: TextFormField(
            // To programmatically control what is shown in the TextField, we use a TextEditingController
            controller: _controller,
            // Focus on this field when it appears on a page.
            autofocus: true,
            focusNode: _focusNode,
            // Decoration
            decoration: const InputDecoration(
              border: OutlineInputBorder(
                  borderRadius: BorderRadius.all(
                Radius.circular(20),
              )),
              // Set a search icon as prefix.
              prefixIcon: Icon(Icons.search),
              // Hint text to help the user.
              hintText: 'Type an asset ID or an asset name',
            ),
            // =====================================================================
            // You can't run a query without searching for, at least, a character
            validator: (value) {
              if (value == null || value.isEmpty) {
                return "You can't do an empty search";
              } else {
                return null;
              }
            },
            // =====================================================================
            // If we enter the field, we elect the text
            onTap: () {
              _focusNode.requestFocus();
              _controller.selection = TextSelection(
                baseOffset: 0,
                extentOffset: _controller.text.length,
              );
            },
            // =====================================================================
            // If something is typed in the field, we keep track of it
            onChanged: (value) => ref.watch(assetSearchFieldValueProvider.notifier).update((state) => value),
            // =====================================================================
            // If the user press "enter", we launch a search
            onFieldSubmitted: (value) {
              if (_formKey.currentState!.validate()) {
                // We update the provider with what the user is searching for
                ref.watch(assetSearchQueryProvider.notifier).setQuery(value);
                // We go the search page where results are displayed (if not already on it).
                context.go(Uri(path: assetSearchRoutePath, queryParameters: {queryParameter: value}).toString());
              }
              // Valid or not, we set the focus
              _focusNode.requestFocus();
              _controller.selection = TextSelection(
                baseOffset: 0,
                extentOffset: _controller.text.length,
              );
            },
          ),
        ));
  }
}
