import 'package:flutter/material.dart';

// Search text field.
Widget searchTextField(BuildContext context, String q) {
  // A widget that sizes its child to a fraction of the total available space.
  return const FractionallySizedBox(
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
  );
}
