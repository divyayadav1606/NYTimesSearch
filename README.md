# Project 2 - *NY Times Search*

**NY Times Search** is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: **18** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [x] User can click on "settings" which allows selection of **advanced search options** to filter results
* [x] User can configure advanced search filters such as:
  * [x] Begin Date (using a date picker)
  * [x] News desk values (Arts, Style, Sports, Travel, Science & Poltics)
  * [x] Sort order (oldest or newest)
* [x] Subsequent searches have any filters applied to the search results
* [x] User can tap on any article in results to view the contents in an embedded browser.
* [x] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* [x] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
      * [x] Put delay in network requests to avoid 429 error and resend request
* [x] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [x] User can **share an article link** to their friends or email it to themselves
* [x] Replaced Filter Settings Activity with a lightweight modal overlay

The following **bonus** features are implemented:

* [x] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [x] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
    * [x] Mix of both Butterknife and Android Data Binding used
* [x] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [x] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [x] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [x] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [x] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)

The following **additional** features are implemented:

* [x] UI
    * [x] Show article snippet with Publish Date
    * [x] BottomNavigation Bar used with Fragments to display News/Bookmarks/Trending
    * [x] Scroll to top at the click of a button
    * [x] Use different layouts for Landscape & Potrait mode. 2 colums in potrain and 3 colums in ladscape
    * [x] Use SnackBar for reporting error messages
    * [x] Progress bar
* [x] Save filter settings in Shared preference to load news when user reopens the app
* [x] Put API key in a config properties file
* [x] Save instance so that is not fetched when screen rotates
* [ ] Save the bookmarks in SQLite and can be viewed even when no connectivity
* [ ] Try sending thumbnail with url in share action

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='' title='Video Walkthrough' width='' alt='Video Walkthrough' />

## Notes

1. Never make last minute big feature additions :)
 - RecyclerView with Cursor adapter,
 - Fragment onCreateView method was called twice. It drove me crazy

## Open-source libraries used

- [Retrofit] (http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
- [ButterKnife] (http://jakewharton.github.io/butterknife/) - Field and method binding for Android views
- [Recyclerview click] (http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/) - Getting your clicks on RecyclerView
- [Glide] (https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling

## License

    Copyright [2017] [Divya Yadav]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
