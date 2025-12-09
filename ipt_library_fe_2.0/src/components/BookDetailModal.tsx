import { useEffect, useState } from 'react';
import { X, BookOpen, AlertCircle } from 'lucide-react';

type ApiResponse<T> = {
  message: string;
  data: T;
};

type BookDetail = {
  id: number;
  isbn: string;
  description: string;
  author: string;
};

type BookDetailModalProps = {
  isOpen: boolean;
  onClose: () => void;
  bookId: number | null;
  token: string | null;
  apiBase: string;
};

export const BookDetailModal = ({
                                  isOpen,
                                  onClose,
                                  bookId,
                                  token,
                                  apiBase,
                                }: BookDetailModalProps) => {
  const [book, setBook] = useState<BookDetail | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isOpen) return;
    if (!bookId) return;
    if (!token) {
      setError('Bạn cần đăng nhập để xem chi tiết sách.');
      return;
    }

    const fetchDetail = async () => {
      setIsLoading(true);
      setError('');
      setBook(null);

      try {
        const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();

        const res = await fetch(`${apiBase}/books/${bookId}`, {
          method: 'GET',
          headers: {
            Authorization: `${tokenType} ${token}`,
          },
        });

        if (!res.ok) {
          const text = await res.text().catch(() => '');
          throw new Error(`Lỗi tải chi tiết sách: ${res.status} ${text}`);
        }

        const raw: ApiResponse<BookDetail> = await res.json();

        // backend trả ApiResponse("Book got by id", book)
        setBook(raw.data);
      } catch (err: any) {
        console.error('[BOOK DETAIL] error', err);
        setError(err?.message || 'Không thể tải chi tiết sách.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchDetail();
  }, [isOpen, bookId, token, apiBase]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="bg-white rounded-lg shadow-lg w-full max-w-lg mx-4 overflow-hidden">
        <div className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">Chi tiết sách</h2>
          <button
            onClick={onClose}
            className="p-1 rounded-full hover:bg-gray-100 transition-colors"
          >
            <X className="h-5 w-5 text-gray-500" />
          </button>
        </div>

        <div className="p-6">
          {isLoading && (
            <div className="flex items-center justify-center py-8">
              <span className="text-gray-600">Đang tải dữ liệu...</span>
            </div>
          )}

          {!isLoading && error && (
            <div className="flex items-start bg-red-50 border border-red-200 rounded-md p-4">
              <AlertCircle className="h-5 w-5 text-red-500 mr-3 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          {!isLoading && !error && book && (
            <div className="space-y-4">
              <div className="flex items-center space-x-4">
                <div className="h-12 w-12 rounded-full bg-primary-100 flex items-center justify-center">
                  <BookOpen className="h-6 w-6 text-primary" />
                </div>
                <div>
                  <h3 className="text-xl font-bold text-gray-900">
                    {book.description || 'Không có tên'}
                  </h3>
                  {book.author && (
                    <p className="text-sm text-gray-600">Tác giả: {book.author}</p>
                  )}
                </div>
              </div>

              <div className="border-t border-gray-200 pt-4 space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-gray-600">ID:</span>
                  <span className="font-mono text-gray-900">{book.id}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">ISBN:</span>
                  <span className="font-mono text-gray-900">
                    {book.isbn || 'N/A'}
                  </span>
                </div>
              </div>
            </div>
          )}
        </div>

        <div className="px-6 py-4 border-t border-gray-200 flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-gray-700 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
};
