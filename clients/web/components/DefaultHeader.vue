<template>
  <!-- ============================================================================================================= -->
  <!-- Our header only has a button to register an asset -->
  <!-- ============================================================================================================= -->

  <!--
        Header.
          - flex: The Flexbox module aims at providing a more efficient way to lay out, align and distribute space among
          items in a container, even when their size is unknown and/or dynamic.
          - items-center to align items along the center of the container’s cross axis (useless when we have only one).
          - justify-between to justify items along the container’s main axis such that there is an equal amount of space
          between each item.
          - px-{size} to add horizontal padding.
          - py-{size} to add vertical padding.
          - bg-{color} to set the background color.
  -->
  <header class="flex items-center justify-end px-4 py-4 bg-white">
    <button
        class="px-4 py-2 font-bold text-white bg-blue-500 rounded hover:bg-blue-700"
    >Add your asset
    </button>
  </header>
</template>