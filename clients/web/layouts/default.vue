<template>
  <!-- ============================================================================================================= -->
  <!-- Default layout.                                                                                               -->
  <!-- ============================================================================================================= -->

  <!--
      CSS explanation:
        - flex: The Flexbox module aims at providing a more efficient way to lay out, align and distribute space among
        items in a container, even when their size is unknown and/or dynamic.
        - flex-col to position flex items vertically.
        - h-screen to make an element span the entire height of the viewport (Usefull to create a footer).
        - justify-between to justify items along the container’s main axis such that there is an equal amount of space
        between each item.
  -->

  <div class="flex flex-col h-screen justify-between">

    <!-- Our header -->
    <DefaultHeader/>

    <!-- Content -->
    <slot/>

    <!-- Our footer -->
    <DefaultFooter/>

  </div>

</template>