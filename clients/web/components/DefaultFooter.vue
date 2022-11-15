<template>
  <!-- ============================================================================================================= -->
  <!-- Our footer allows the user to know more about Royllo (website, modal page about, twitter, github & email)     -->
  <!-- ============================================================================================================= -->

  <footer class="p-4 bg-gray-100 rounded-b-lg shadow md:flex md:items-center md:justify-between dark:bg-gray-800 mt-11">

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

<script setup>

// Current year used to write the copyright year.
const currentYear = new Date().getFullYear();

</script>