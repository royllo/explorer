<template>

  <div class="flex flex-wrap flex-row justify-center">

    <!-- ============================================================================================================= -->
    <!-- Add request form -->
    <div
        class="block px-2 py-2 w-full max-w-sm bg-white rounded-lg border border-gray-200 shadow-md sm:p-6 md:p-8 dark:bg-gray-800 dark:border-gray-700">
      <form class="space-y-6" action="#">
        <h5 class="text-xl font-medium text-gray-900 dark:text-white">Declare an asset</h5>
        <div>
          <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Your email</label>
          <input type="email" name="email" id="email"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
                 placeholder="" required>
        </div>
        <button type="submit"
                class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
          Add this asset
        </button>
      </form>
    </div>
    <!-- ============================================================================================================= -->

    &nbsp;&nbsp;&nbsp;

    <!-- ============================================================================================================= -->
    <!-- Opened requests -->
    <div class="block px-2 py-2 w-full max-w-md bg-white rounded-lg border shadow-md sm:p-8 dark:bg-gray-800 dark:border-gray-700">
      <div class="flex justify-between items-center mb-4">
        <h5 class="text-xl font-bold leading-none text-gray-900 dark:text-white">Opened requests</h5>
      </div>
      <div class="flow-root">
        <ul role="list" class="divide-y divide-gray-200 dark:divide-gray-700">
          <li class="py-3 sm:py-4" v-for="request in data?.openedRequests" :key="request.id">
            <div class="flex items-center space-x-4">
              <div class="flex-1 min-w-0">
                <p class="text-sm font-medium truncate dark:text-white">
                  <NuxtLink :to="`/request/${request.id}`">
                    Request n°{{ request.id }}
                  </NuxtLink>
                </p>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!-- ============================================================================================================= -->

  </div>
</template>

<script lang="ts" setup>

// =====================================================================================================================
// Executing the graphQL query that retrieves an asset.
const query = gql`
    query openedRequests {
      openedRequests {
        id
  }
}`;
const variables = {};
const {data} = await useAsyncQuery(query, variables);

</script>