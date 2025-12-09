import { ReactNode, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, X, LogOut, User } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import logo from "../images/logo.png";

interface LayoutProps {
  children: ReactNode;
}

export const Layout = ({ children }: LayoutProps) => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { user, logout } = useAuth();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  const navigation = [
    { name: 'Trang chủ', path: '/' },
    { name: 'Danh sách sách', path: '/books', protected: true },
    { name: 'Danh sách người dùng', path: '/users', protected: true },
  ];

  const handleLogout = () => {
    logout();
    setIsMobileMenuOpen(false);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="flex items-center space-x-2">
                {/*<BookOpen className="h-8 w-8 text-primary" />*/}
                <img src={logo} alt="Logo" className="max-h-12" />
                <span className="text-xl font-bold text-primary">InPhuThinh</span>
              </Link>

              <div className="hidden md:ml-10 md:flex md:space-x-8">
                {navigation.map((item) => {
                  if (item.protected && !user) return null;
                  return (
                    <Link
                      key={item.path}
                      to={item.path}
                      className={`inline-flex items-center px-1 pt-1 text-sm font-medium border-b-2 transition-colors ${
                        isActive(item.path)
                          ? 'border-secondary text-primary'
                          : 'border-transparent text-gray-600 hover:text-primary hover:border-gray-300'
                      }`}
                    >
                      {item.name}
                    </Link>
                  );
                })}
              </div>
            </div>

            <div className="hidden md:flex md:items-center md:space-x-4">
              {user ? (
                <>
                  <div className="flex items-center space-x-2 text-gray-700">
                    <User className="h-5 w-5 text-primary" />
                    <span className="text-sm font-medium">{user.name || user.email}</span>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary-700 transition-colors"
                  >
                    <LogOut className="h-4 w-4 mr-2" />
                    Đăng xuất
                  </button>
                </>
              ) : (
                <>
                  <Link
                    to="/login"
                    className="inline-flex items-center px-4 py-2 border border-primary text-sm font-medium rounded-md text-primary hover:bg-primary-50 transition-colors"
                  >
                    Đăng nhập
                  </Link>
                  <Link
                    to="/register"
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-secondary hover:bg-secondary-600 transition-colors"
                  >
                    Đăng ký
                  </Link>
                </>
              )}
            </div>

            <div className="flex items-center md:hidden">
              <button
                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:text-primary hover:bg-gray-100 transition-colors"
              >
                {isMobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
              </button>
            </div>
          </div>
        </div>

        {isMobileMenuOpen && (
          <div className="md:hidden border-t border-gray-200">
            <div className="px-2 pt-2 pb-3 space-y-1">
              {navigation.map((item) => {
                if (item.protected && !user) return null;
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    onClick={() => setIsMobileMenuOpen(false)}
                    className={`block px-3 py-2 rounded-md text-base font-medium transition-colors ${
                      isActive(item.path)
                        ? 'bg-primary-50 text-primary'
                        : 'text-gray-700 hover:bg-gray-100 hover:text-primary'
                    }`}
                  >
                    {item.name}
                  </Link>
                );
              })}
            </div>

            <div className="pt-4 pb-3 border-t border-gray-200">
              {user ? (
                <div className="px-4 space-y-3">
                  <div className="flex items-center space-x-2">
                    <User className="h-5 w-5 text-primary" />
                    <span className="text-sm font-medium text-gray-700">{user.name || user.email}</span>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary-700 transition-colors"
                  >
                    <LogOut className="h-4 w-4 mr-2" />
                    Đăng xuất
                  </button>
                </div>
              ) : (
                <div className="px-4 space-y-2">
                  <Link
                    to="/login"
                    onClick={() => setIsMobileMenuOpen(false)}
                    className="block w-full text-center px-4 py-2 border border-primary text-sm font-medium rounded-md text-primary hover:bg-primary-50 transition-colors"
                  >
                    Đăng nhập
                  </Link>
                  <Link
                    to="/register"
                    onClick={() => setIsMobileMenuOpen(false)}
                    className="block w-full text-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-secondary hover:bg-secondary-600 transition-colors"
                  >
                    Đăng ký
                  </Link>
                </div>
              )}
            </div>
          </div>
        )}
      </nav>

      <main className="flex-1">{children}</main>

      <footer className="bg-primary text-white mt-auto">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div>
              <h3 className="text-lg font-semibold mb-4">Công ty TNHH In và DVTM Phú Thịnh</h3>
              <p className="text-primary-100 text-sm">
                Công ty in ấn hàng đầu, cung cấp dịch vụ in ấn chất lượng cao và quản lý xuất bản chuyên nghiệp.
              </p>
            </div>
            <div>
              <h3 className="text-lg font-semibold mb-4">Liên hệ</h3>
              <p className="text-primary-100 text-sm">Email: ipt@inphuthinh.vn</p>
              <p className="text-primary-100 text-sm">Điện thoại: +84 243 763 5590</p>
            </div>
            <div>
              <h3 className="text-lg font-semibold mb-4">Thông tin</h3>
              <p className="text-primary-100 text-sm">© 2025 InPhuThinh. All rights reserved.</p>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};
