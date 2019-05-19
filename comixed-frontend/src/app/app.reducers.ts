import { userReducer } from 'app/reducers/user.reducer';
import { importingReducer } from 'app/reducers/importing.reducer';
import { libraryReducer } from 'app/reducers/library.reducer';
import { libraryFilterReducer } from 'app/reducers/library-filter.reducer';
import { libraryDisplayReducer } from 'app/reducers/library-display.reducer';
import { singleComicScrapingReducer } from 'app/reducers/single-comic-scraping.reducer';
import { multipleComicsScrapingReducer } from 'app/reducers/multiple-comics-scraping.reducer';
import { duplicatesReducer } from 'app/reducers/duplicates.reducer';
import { userAdminReducer } from 'app/reducers/user-admin.reducer';
import { readingListReducer } from 'app/reducers/reading-list.reducer';

export const REDUCERS = {
  user: userReducer,
  importing: importingReducer,
  library: libraryReducer,
  library_filter: libraryFilterReducer,
  library_display: libraryDisplayReducer,
  single_comic_scraping: singleComicScrapingReducer,
  multiple_comic_scraping: multipleComicsScrapingReducer,
  duplicates: duplicatesReducer,
  user_admin: userAdminReducer,
  reading_lists: readingListReducer
};