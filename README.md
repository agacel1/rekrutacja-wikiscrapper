# WIKI SCRAPPER

There is a hypothesis that from any wikipedia page, you can access any other page via included links at the bottom of the site.
For the purpose of this task we'll assume that this hypothesis is true. Your task is to implement a mechanism that will scrap
whole wikipedia from any root page and save it to the DB.

* You don't have to worry about DB part, it's already implemented.
* You don't have to worry about page retrieval part, it's already implemented.

## Acceptance Criteria
1. After using POST `/wiki/scrap` with root link that does exist, program should scrap all of the pages in wikipedia and save them to the database
   1. Body of the POST: `"http://wikiscrapper.test/site1"` (with `"`)
   2. When `html` profile is being set it should scrap wiki using html client (use `HtmlWikiClient`)
   3. When `json` profile is being set it should scrap wiki using json client (use `JsonWikiClient`)
2. After using POST `/wiki/scrap` with root link that doesn't exist, program should return 404


## Implementation steps

1. Implement WikiScrapper class in domain module
   1. Consider cases when wikiReader throws `WikiPageNotFound` error
   2. Write tests for domain module (in order to test you need to mock all plugins - repository & wiki reader)
   3. Think about looped wikiPages (for example wikiPage1 -> wikiPage2 & wikiPage2 -> wikiPage1)
2. Implement `WikiReader` for both clients `html` & `json`
3. Fix `WikiScrapperConfiguration` to return correct implementation for both `readers`
4. Implement `WikiScrapperResource` to call `WikiScrapper`
   1. 404 httpStatus should be returned for not existing `WikiPage`
   2. Implement some automated tests for rest requests (check 404 & 200 case)
   3. Implement some automated tests which uses `json` profile and read some `json` wikipage

## Hints

1. It's allowed to use any testing framework (for example Mockito for mocking)
2. It's allowed & recommended using external libraries to read both `html` & `json` files
3. Domain module shouldn't have dependency to any other module


## Submitting task

Task implementation should be shared on some public repository (for example github)


        