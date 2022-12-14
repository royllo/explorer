import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../main.dart';

// Search text field.

class SearchTextField extends ConsumerStatefulWidget {
  // Constructor.
  const SearchTextField({super.key});

  @override
  ConsumerState<SearchTextField> createState() => _SearchTextFieldState();
}

class _SearchTextFieldState extends ConsumerState<SearchTextField> {
  // Text editing controller.
  final _controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    _controller.text = ref.read(searchedValueProvider);
    // fix things here
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
      widthFactor: 0.7,
      // The search field
      child: TextField(
        // To programmatically control what is shown in the TextField, we use a TextEditingController.
        controller: _controller,
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
        onChanged: (value) =>
            ref.watch(searchedValueProvider.notifier).update((state) => value),
      ),
    );
  }
}
