import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../main.dart';

// Search text field.
class SearchField extends ConsumerStatefulWidget {
  // Constructor.
  const SearchField({super.key});

  @override
  ConsumerState<SearchField> createState() => _SearchFieldState();
}

class _SearchFieldState extends ConsumerState<SearchField> {
  // Text editing controller.
  final _controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    // When the field is created, we retrieve the value from the provider.
    _controller.text = ref.read(searchFieldValueProvider);
    // Select the text field content when it's created.
    _controller.selection = TextSelection(
      baseOffset: 0,
      extentOffset: _controller.text.length,
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // A widget that sizes its child to a fraction of the total available space.
    return FractionallySizedBox(
      widthFactor: 0.5,
      // The search field
      child: TextField(
        // To programmatically control what is shown in the TextField, we use a TextEditingController.
        controller: _controller,
        // Focus on this field when it appears on a page.
        autofocus: true,
        // Set the radius.
        decoration: const InputDecoration(
          border: OutlineInputBorder(
              borderRadius: BorderRadius.all(
            Radius.circular(20),
          )),
          // Set a search icon as prefix.
          prefixIcon: Icon(Icons.search),
          // Hint text to help the user.
          hintText: 'Type an asset id or an asset name',
        ),
        onChanged: (value) => ref
            .watch(searchFieldValueProvider.notifier)
            .update((state) => value),
        onSubmitted: (value) {
          // We update the searched value.
          ref.watch(searchedValueProvider.notifier)
          .update((state) => value);
          // We go the search page where results are displayed.
          context.go(
              Uri(path: '/search', queryParameters: {'q': value}).toString());
        },
      ),
    );
  }
}
