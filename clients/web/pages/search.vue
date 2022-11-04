<template>
  <!-- ============================================================================================================= -->
  <!-- Search engine and result screen.                                                                              -->
  <!-- ============================================================================================================= -->
  <div class="flex flex-col h-screen items-center">

    <!-- Search form -->
    <SearchAssetForm
        :searchedValue="q"
    />

    <!-- Search results -->
    <div class="mt-11">
      <!-- No value for the "q" query string parameter -->
      <p v-if="!q">Your search must, at least, contains a character!</p>

      <p>There are {{ data?.userByUsername?.length || 0 }} user.</p>
      <p>There are {{ data?.userByUsername }} user.</p>
      <p>There are {{ data?.userByUsername?.id }} user.</p>

      <AssetPreview
          assetId="jmp"
      />
    </div>

  </div>
</template>

<script lang="ts" setup>
// "q" is the value search by the use.
const q = computed(() => {
  return useRoute().query.q;
});

// GraphQL query
const query = gql`
  query userByUsername($username: String!) {
       userByUsername(username: $username) {
           id
           username
       }
   }`;
const variables = { username: 'anonymous' };
const { data } = await useAsyncQuery(query, variables);


// const query = gql`
//   query userByUsername($username: String!) {
//       userByUsername(username: $username) {
//           id
//           username
//       }
//   }`;
// const variables = { username: q};
//
// const { data } = await useAsyncQuery(query, variables);
// console.log("ici " + data.value);
</script>