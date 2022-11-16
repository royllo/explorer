<template>
  <!-- ============================================================================================================= -->
  <!-- Our footer allows the user to know more about Royllo (website, github, twitter & email)                       -->
  <!-- ============================================================================================================= -->

  <footer class="p-4 bg-gray-100 rounded-b-lg shadow md:flex md:items-center md:justify-between dark:bg-gray-800 mt-11">

    <!-- Left part : Royllo copyright and link -->
    <span class="text-sm text-gray-500 sm:text-center">
      © {{ currentYear }} <NuxtLink to="https://www.royllo.org"
                                    class="hover:underline"
                                    target="royllo_website">Royllo</NuxtLink>
      -
       <NuxtLink  to="https://github.com/royllo/explorer/blob/development/LICENSE"
                  class="hover:underline"
                  target="royllo_website">GNU General Public License v3.0</NuxtLink>
    </span>

    <!-- Right part : Links to know more about Royllo -->
    <ul class="flex flex-wrap items-center mt-3 text-sm text-gray-500 sm:mt-0">
      <li>
        <NuxtLink to="https://github.com/royllo/explorer" class="mr-4 hover:underline md:mr-6" target="github">Github</NuxtLink>
      </li>
      <li>
        <NuxtLink to="https://twitter.com/royllo_systems" class="mr-4 hover:underline md:mr-6" target="twitter">Twitter</NuxtLink>
      </li>
      <li>
        <NuxtLink to="mailto:contact@royllo.org" class="hover:underline">Contact</NuxtLink>
      </li>
    </ul>

  </footer>

</template>

<script setup>

// Current year used to write the copyright year.
const currentYear = new Date().getFullYear();

</script>