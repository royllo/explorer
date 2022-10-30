<script setup>
// Current year used to write the copyright year.
const currentYear = new Date().getFullYear();
</script>

<template>
  <!-- ============================================================================================================= -->
  <!-- Our footer allows the user to know more about Royllo (website, modal page about, twitter, github & email )    -->
  <!-- ============================================================================================================= -->
  <!--
        CSS explanation:
          - flex: The Flexbox module aims at providing a more efficient way to lay out, align and distribute space among
          items in a container, even when their size is unknown and/or dynamic.
          - flex-wrap to allow flex items to wrap.
          - items-center to align items along the center of the container’s cross axis (useless when we have only one).
          - justify-between to justify items along the container’s main axis such that there is an equal amount of space
          between each item.
          - px-{size} to add horizontal padding.
          - py-{size} to add vertical padding.
          - bg-{color} to set the background color.
          - .rounded-sm, .rounded, or .rounded-lg to apply different border radius sizes to an element.
          - text-{size} to control the font size of an element using.
          - m{t|r|b|l}-{size} to control the margin on one side of an element.
  -->
  <footer class="p-4 bg-gray-100 rounded-b-lg shadow md:flex md:items-center md:justify-between md:p-6 dark:bg-gray-800">

    <!-- Left part : Royllo copyright and link -->
    <span class="text-sm text-gray-500 sm:text-center">
      © {{ currentYear }} <a href="https://www.royllo.org"
                             class="hover:underline"
                             target="royllo_website">Royllo</a>
      -
       <a href="https://github.com/royllo/explorer/blob/development/LICENSE"
          class="hover:underline"
          target="royllo_website">GNU General Public License v3.0</a>
      </span>

    <!-- Right part : Links to know more about Royllo -->
    <ul class="flex flex-wrap items-center mt-3 text-sm text-gray-500 sm:mt-0">
      <li>
        <a href="#" class="mr-4 hover:underline md:mr-6 ">About</a>
      </li>
      <li>
        <a href="https://github.com/royllo/explorer" class="mr-4 hover:underline md:mr-6" target="github">Github</a>
      </li>
      <li>
        <a href="https://twitter.com/royllo_systems" class="mr-4 hover:underline md:mr-6" target="twitter">Twitter</a>
      </li>
      <li>
        <a href="mailto:contact@royllo.org" class="hover:underline">Contact</a>
      </li>
    </ul>

  </footer>

</template>