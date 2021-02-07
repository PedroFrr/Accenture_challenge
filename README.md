# Accenture_challenge

- MVVM architecture with repository pattern
- Room for DB integration, supports offline use
- Paging 3.0 used to paginate API response, data is fetched as the user scrolls on the Pokemon List
- When the user is on Portrait mode the only 1 item is shown per column, when in landscape two items are shown
- Hilt is used to inject dependencies on the application
- Retrofit used to communicate with the remote API
- When the user sets a Pokemon as a Favorite a POST request is sent to a WebHook (see here: https://webhook.site/#!/ac9a6506-0aad-4fd1-97c4-3d76d10d9751/143d4408-8517-4487-b656-60aac5b00eec/1) 
- Small Unit Test is done on the Pokemon DAO using Hilt for injecting the in memory database