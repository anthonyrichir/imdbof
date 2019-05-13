import { ICategory } from 'app/shared/model/category.model';

export interface IMovie {
    id?: number;
    title?: string;
    originalTitle?: string;
    voteCount?: number;
    voteAverage?: number;
    video?: boolean;
    popularity?: number;
    posterPath?: string;
    originalLanguage?: string;
    backdropPath?: string;
    adult?: boolean;
    releaseDate?: string;
    overview?: any;
    categories?: ICategory[];
}

export class Movie implements IMovie {
    constructor(
        public id?: number,
        public title?: string,
        public originalTitle?: string,
        public voteCount?: number,
        public voteAverage?: number,
        public video?: boolean,
        public popularity?: number,
        public posterPath?: string,
        public originalLanguage?: string,
        public backdropPath?: string,
        public adult?: boolean,
        public releaseDate?: string,
        public overview?: any,
        public categories?: ICategory[]
    ) {
        this.video = this.video || false;
        this.adult = this.adult || false;
    }
}
