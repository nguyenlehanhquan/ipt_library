import { useEffect, useState } from 'react';
import axios from 'axios';
import { Trash2, AlertCircle, User as UserIcon, RefreshCw } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { User } from '../types/auth';

// Kiểu giống backend
type ApiResponse<T> = {
  message: string;
  data: T;
  errorCode?: string;
};

// Kiểu user BE trả về: id, fullname, username
type ApiUser = {
  id: number;
  fullName: string;
  username: string;
};

export const UserList = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const { token, isLoading: authLoading } = useAuth();

  // Base URL theo pattern của dự án
  const API_BASE =
    window.location.hostname === 'localhost'
      ? 'http://localhost:8080'
      : '';

  const fetchUsers = async () => {
    if (!token) return;
    setIsLoading(true);
    setError('');

    try {
      const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();

      const response = await axios.get<ApiResponse<ApiUser[]>>(
        `${API_BASE}/users`,
        {
          headers: {
            Authorization: `${tokenType} ${token}`,
          },
        }
      );

      const apiRes = response.data;

      // Backend: ApiResponse("Get all users", List<UserResponse>)
      const apiUsers = Array.isArray(apiRes.data) ? apiRes.data : [];

      // Map từ ApiUser -> User (view model của FE)
      const userList: User[] = apiUsers.map((u) => ({
        id: String(u.id),
        fullName: u.fullName,
        username: u.username,
      }));

      setUsers(userList);
    } catch (err: any) {
      console.error('[USERS] error', err);
      setError(
        err.response?.data?.message ||
        err.message ||
        'Không thể tải danh sách người dùng.'
      );
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (authLoading) return;
    if (token) {
      fetchUsers();
    }
  }, [token, authLoading]);

  const handleDelete = async (id: string) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa người dùng này không?')) return;

    try {
      const tokenType = (localStorage.getItem('tokenType') || 'Bearer').trim();

      // Backend: @DeleteMapping("/{id}") trả về ApiResponse("User deleted", null)
      await axios.delete<ApiResponse<null>>(
        `${API_BASE}/users/${id}`,
        {
          headers: {
            Authorization: `${tokenType} ${token}`,
          },
        }
      );

      // Cập nhật UI sau khi xóa thành công
      setUsers((prev) => prev.filter((u) => String(u.id) !== String(id)));
      alert('Đã xóa người dùng thành công!');
    } catch (err: any) {
      console.error('Delete error', err);
      alert('Xóa thất bại: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8 flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Danh sách người dùng</h1>
            <p className="mt-2 text-gray-600">Quản lý tài khoản người dùng trong hệ thống</p>
          </div>
          <button
            onClick={fetchUsers}
            disabled={isLoading}
            className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors disabled:opacity-50"
          >
            <RefreshCw className={`h-4 w-4 mr-2 ${isLoading ? 'animate-spin' : ''}`} />
            Làm mới
          </button>
        </div>

        {error && (
          <div className="mb-6 bg-red-50 border border-red-200 rounded-md p-4 flex items-start">
            <AlertCircle className="h-5 w-5 text-red-500 mr-3 flex-shrink-0 mt-0.5" />
            <span className="text-sm text-red-700">{error}</span>
          </div>
        )}

        <div className="bg-white shadow-sm rounded-lg overflow-hidden border border-gray-200">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
              <tr>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-md font-medium text-gray-500 uppercase tracking-wider"
                >
                  Fullname
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-md font-medium text-gray-500 uppercase tracking-wider"
                >
                  Username
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-md font-medium text-gray-500 uppercase tracking-wider"
                >
                  ID
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-right text-md font-medium text-gray-500 uppercase tracking-wider"
                >
                  Action
                </th>
              </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
              {isLoading ? (
                <tr>
                  <td colSpan={4} className="px-6 py-12 text-center text-gray-500">
                    <RefreshCw className="h-8 w-8 text-primary animate-spin mx-auto mb-2" />
                    Đang tải dữ liệu...
                  </td>
                </tr>
              ) : users.length === 0 ? (
                <tr>
                  <td colSpan={4} className="px-6 py-12 text-center text-gray-500">
                    Chưa có người dùng nào trong hệ thống.
                  </td>
                </tr>
              ) : (
                users.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="flex-shrink-0 h-10 w-10 bg-primary-100 rounded-full flex items-center justify-center">
                          <UserIcon className="h-5 w-5 text-primary" />
                        </div>
                        <div className="ml-4">
                          <div className="text-md font-medium text-gray-900">
                            {user.fullName || 'Không có tên'}
                          </div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-md text-gray-900">{user.username}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-gray-500 font-mono text-md">{user.id}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <div className="flex items-center justify-end space-x-3">
                        <button
                          onClick={() => handleDelete(String(user.id))}
                          className="text-red-600 hover:text-red-900 transition-colors"
                          title="Xóa"
                        >
                          <Trash2 className="h-5 w-5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};
