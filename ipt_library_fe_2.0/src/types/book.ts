export interface Book {
  id: number;
  isbn?: string;
  title?: string;        // optional vì BE không trả
  description?: string;
  // author?: string | null;
  // category?: string;
  // pages?: number;
  // price?: number;
  coverImage?: string;
  // stock?: number;
}
