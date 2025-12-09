import { useState, useEffect, useRef, ChangeEvent } from 'react';
import { BookOpen, Search, RefreshCw, AlertCircle /*, Filter*/ } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { Book } from '../types/book';
import { BookDetailModal } from '../components/BookDetailModal';


export const BookList = () => {
  const [books, setBooks] = useState<Book[]>([]);
  const [filteredBooks, setFilteredBooks] = useState<Book[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const [isImporting, setIsImporting] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const { token, isLoading: authLoading } = useAuth();

  const [selectedBookId, setSelectedBookId] = useState<number | null>(null);


  const API_BASE =
    window.location.hostname === 'localhost'
      ? 'http://localhost:8080'
      : '';

  const categories = ['all', 'Văn học', 'Khoa học', 'Kinh doanh', 'Giáo dục', 'Thiếu nhi'];

  const fetchBooks = async () => {
    if (!token) return;
    setIsLoading(true);
    setError('');

    try {
      const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();

      const response = await fetch(`${API_BASE}/books`, {
        method: 'GET',
        headers: {
          Authorization: `${tokenType} ${token}`,
        },
      });

      console.log('[BOOKS] status', response.status);

      if (!response.ok) {
        const text = await response.text().catch(() => '');
        throw new Error(`Failed to fetch books: ${response.status} ${text}`);
      }

      const raw = await response.json();
      console.log('[BOOKS] raw response', raw);

      let items: any[] = [];
      if (Array.isArray(raw)) {
        items = raw;
      } else if (Array.isArray(raw?.data)) {
        items = raw.data;
      } else if (Array.isArray(raw?.data?.content)) {
        items = raw.data.content;
      } else if (Array.isArray(raw?.content)) {
        items = raw.content;
      } else {
        items = [];
      }

      console.log('[BOOKS] items length', items.length);

      const normalized: Book[] = items.map((b: any) => ({
        id: b.id,
        isbn: b.isbn ?? '',
        title: b.title ?? b.description ?? `Book #${b.id}`,
        description: b.description ?? '',
        author: b.author ?? 'N/A',
        category: b.category ?? undefined,
        pages: b.pages ?? undefined,
        price: b.price ?? undefined,
        coverImage: b.coverImage ?? undefined,
        stock: b.stock ?? undefined,
      }));

      setBooks(normalized);
      setFilteredBooks(normalized);
    } catch (err: any) {
      console.error('[BOOKS] error', err);
      setError(err?.message || 'Không thể tải danh sách sách. Vui lòng thử lại sau.');
      setBooks([]);
      setFilteredBooks([]);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (authLoading) return;
    if (!token) return;
    fetchBooks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token, authLoading]);

  useEffect(() => {
    let result = books;

    if (searchTerm) {
      const q = searchTerm.toLowerCase();
      result = result.filter((book) =>
        (book.title ?? '').toLowerCase().includes(q) ||
        (book.isbn ?? '').toLowerCase().includes(q)
      );
    }

    // if (selectedCategory !== 'all') {
    //   result = result.filter((book) => (book.category ?? '') === selectedCategory);
    // }

    setFilteredBooks(result);
  }, [searchTerm, selectedCategory, books]);

  // const formatPrice = (price?: number) => {
  //   if (!price) return 'N/A';
  //   return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
  // };

  const handleImportClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = async (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!token) {
      alert('Bạn cần đăng nhập để import.');
      return;
    }

    setIsImporting(true);
    setError('');

    try {
      const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();
      const formData = new FormData();
      formData.append('file', file);

      const response = await fetch(`${API_BASE}/book_contracts/importexcel`, {
        method: 'POST',
        headers: {
          Authorization: `${tokenType} ${token}`,
          // KHÔNG set Content-Type, để browser tự set boundary cho multipart/form-data
        } as any,
        body: formData,
      });

      if (!response.ok) {
        const text = await response.text().catch(() => '');
        throw new Error(`Import thất bại: ${response.status} ${text}`);
      }

      alert('Import Excel thành công!');
      // Sau khi import, có thể reload danh sách
      fetchBooks();
    } catch (err: any) {
      console.error('[IMPORT EXCEL] error', err);
      alert(err?.message || 'Import Excel thất bại.');
    } finally {
      setIsImporting(false);
      // reset input để lần sau chọn lại cùng file vẫn trigger change
      e.target.value = '';
    }
  };

  const handleExportClick = async () => {
    if (!token) {
      alert('Bạn cần đăng nhập để export.');
      return;
    }

    setIsExporting(true);
    setError('');

    try {
      const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();

      const response = await fetch(`${API_BASE}/book_contracts/exportexcel`, {
        method: 'POST',
        headers: {
          Authorization: `${tokenType} ${token}`,
        },
      });

      if (!response.ok) {
        const text = await response.text().catch(() => '');
        throw new Error(`Export thất bại: ${response.status} ${text}`);
      }

      const blob = await response.blob();

      // TỰ FORMAT TÊN FILE GIỐNG HỆ JAVA: book_contracts_yyyyMMddHHmmss.xlsx
      const now = new Date();
      const pad = (n: number) => n.toString().padStart(2, '0');
      const yyyy = now.getFullYear();
      const MM = pad(now.getMonth() + 1);
      const dd = pad(now.getDate());
      const HH = pad(now.getHours());
      const mm = pad(now.getMinutes());
      const ss = pad(now.getSeconds());
      const fileName = `book_contracts_${yyyy}${MM}${dd}${HH}${mm}${ss}.xlsx`;

      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err: any) {
      console.error('[EXPORT EXCEL] error', err);
      alert(err?.message || 'Export Excel thất bại.');
    } finally {
      setIsExporting(false);
    }
  };


  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">Danh sách sách</h1>
              <p className="mt-2 text-gray-600">Quản lý và theo dõi kho sách của công ty</p>
            </div>
            <button
              onClick={fetchBooks}
              disabled={isLoading}
              className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors disabled:opacity-50"
            >
              <RefreshCw className={`h-4 w-4 mr-2 ${isLoading ? 'animate-spin' : ''}`} />
              Làm mới
            </button>
          </div>

          <div className="bg-white rounded-lg shadow-sm p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Ô tìm kiếm */}
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Search className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  type="text"
                  placeholder="Tìm kiếm theo tên, tác giả, ISBN..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors"
                />
              </div>

              {/* CHỖ CATEGORY CŨ - TẠM THỜI COMMENT LẠI */}
              {/*<div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Filter className="h-5 w-5 text-gray-400" />
                </div>
                <select
                  value={selectedCategory}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                  className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-md leading-5 bg-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors appearance-none"
                >
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {category === 'all' ? 'Tất cả danh mục' : category}
                    </option>
                  ))}
                </select>
              </div>*/}

              {/* THAY BẰNG 2 NÚT IMPORT / EXPORT EXCEL */}
              <div className="flex flex-col sm:flex-row sm:justify-end sm:items-center gap-3">
                <input
                  ref={fileInputRef}
                  type="file"
                  accept=".xlsx,.xls"
                  className="hidden"
                  onChange={handleFileChange}
                />
                <button
                  type="button"
                  onClick={handleImportClick}
                  disabled={isImporting}
                  className="inline-flex items-center justify-center px-4 py-2 border border-primary text-sm font-medium rounded-md text-primary bg-white hover:bg-primary-50 transition-colors disabled:opacity-50"
                >
                  {isImporting ? 'Đang import...' : 'Import Excel'}
                </button>
                <button
                  type="button"
                  onClick={handleExportClick}
                  disabled={isExporting}
                  className="inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-secondary hover:bg-secondary-600 transition-colors disabled:opacity-50"
                >
                  {isExporting ? 'Đang export...' : 'Export Excel'}
                </button>
              </div>
            </div>

            <div className="mt-4 flex items-center justify-between text-sm text-gray-600">
              <span>
                Hiển thị <span className="font-semibold text-primary">{filteredBooks.length}</span> / {books.length} sách
              </span>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6 bg-red-50 border border-red-200 rounded-md p-4 flex items-start">
            <AlertCircle className="h-5 w-5 text-red-500 mr-3 flex-shrink-0 mt-0.5" />
            <span className="text-sm text-red-700">{error}</span>
          </div>
        )}

        {isLoading ? (
          <div className="flex items-center justify-center py-12">
            <div className="text-center">
              <RefreshCw className="h-12 w-12 text-primary animate-spin mx-auto mb-4" />
              <p className="text-gray-600">Đang tải dữ liệu...</p>
            </div>
          </div>
        ) : filteredBooks.length === 0 ? (
          <div className="bg-white rounded-lg shadow-sm p-12 text-center">
            <BookOpen className="h-16 w-16 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Không tìm thấy sách</h3>
            <p className="text-gray-600">
              {books.length === 0
                ? 'Chưa có sách nào trong hệ thống. Vui lòng thêm sách mới.'
                : 'Không có sách nào phù hợp với bộ lọc của bạn.'}
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredBooks.map((book) => (
              <div
                key={book.id}
                className="bg-white rounded-lg shadow-sm hover:shadow-lg transition-shadow overflow-hidden group"
              >
                <div className="relative h-48 bg-gradient-to-br from-primary-100 to-secondary-100 flex items-center justify-center">
                  {book.coverImage ? (
                    <img
                      src={book.coverImage}
                      alt={book.title}
                      className="h-full w-full object-cover"
                    />
                  ) : (
                    <BookOpen className="h-20 w-20 text-primary opacity-50 group-hover:scale-110 transition-transform" />
                  )}
                </div>

                <div className="p-6">
                  {/*<div className="mb-2">
                    {book.category && (
                      <span className="inline-block px-2 py-1 text-xs font-semibold text-primary bg-primary-50 rounded">
                        {book.category}
                      </span>
                    )}
                  </div>*/}

                  <h3 className="text-lg font-bold text-gray-900 mb-2 line-clamp-2 group-hover:text-primary transition-colors">
                    {book.title}
                  </h3>

                  {/*<p className="text-sm text-gray-600 mb-4">Tác giả: {book.author}</p>*/}

                  {book.description && (
                    <p className="text-sm text-gray-500 mb-4 line-clamp-2">{book.description}</p>
                  )}

                  <div className="border-t border-gray-200 pt-4 space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="text-gray-600">ISBN:</span>
                      <span className="font-medium text-gray-900">{book.isbn || 'N/A'}</span>
                    </div>
                  </div>

                  <button
                    className="mt-4 w-full px-4 py-2 border border-primary text-sm font-medium rounded-md text-primary hover:bg-primary hover:text-white transition-colors"
                    onClick={() => {
                      if (!book.id) return;
                      // book.id trong Book đang là number hoặc string, convert về number
                      setSelectedBookId(Number(book.id));
                    }}
                  >
                    Xem chi tiết
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      {selectedBookId !== null && (
        <BookDetailModal
          isOpen={selectedBookId !== null}
          onClose={() => setSelectedBookId(null)}
          bookId={selectedBookId}
          token={token}
          apiBase={API_BASE}
        />
      )}
    </div>
  );
};
