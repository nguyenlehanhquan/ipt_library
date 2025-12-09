import { Link } from 'react-router-dom';
import { BookOpen, Printer, Users, Award, ArrowRight } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import logo from "../images/logo.png"

export const Home = () => {
  const { user } = useAuth();

  const features = [
    {
      icon: <Printer className="h-12 w-12 text-primary group-hover:text-white" />,
      title: 'In ấn chất lượng cao',
      description: 'Công nghệ in hiện đại, đảm bảo chất lượng sản phẩm tốt nhất',
    },
    {
      icon: <BookOpen className="h-12 w-12 text-primary group-hover:text-white" />,
      title: 'Quản lý xuất bản',
      description: 'Hệ thống quản lý sách và tài liệu chuyên nghiệp, hiệu quả',
    },
    {
      icon: <Users className="h-12 w-12 text-primary group-hover:text-white" />,
      title: 'Đội ngũ chuyên nghiệp',
      description: 'Đội ngũ nhân viên giàu kinh nghiệm, tận tâm phục vụ',
    },
    {
      icon: <Award className="h-12 w-12 text-primary group-hover:text-white" />,
      title: 'Giải thưởng uy tín',
      description: 'Nhiều giải thưởng về chất lượng và dịch vụ xuất sắc',
    },
  ];

  return (
    <div className="min-h-screen">
      <section className="relative bg-gradient-to-br from-primary to-primary-700 text-white py-20 md:py-32">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
            <div className="space-y-6">
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight">
                Giải pháp in ấn và xuất bản
                <span className="block text-secondary">chuyên nghiệp</span>
              </h1>
              <p className="text-lg md:text-xl text-primary-100">
                In Phú Thịnh cung cấp dịch vụ in ấn chất lượng cao và hệ thống quản lý xuất bản toàn diện cho doanh nghiệp của bạn.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                {user ? (
                  <Link
                    to="/books"
                    className="inline-flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-primary bg-white hover:bg-gray-50 transition-colors"
                  >
                    Xem danh sách sách
                    <ArrowRight className="ml-2 h-5 w-5" />
                  </Link>
                ) : (
                  <>
                    <Link
                      to="/register"
                      className="inline-flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-primary bg-white hover:bg-gray-50 transition-colors"
                    >
                      Bắt đầu ngay
                      <ArrowRight className="ml-2 h-5 w-5" />
                    </Link>
                    <Link
                      to="/login"
                      className="inline-flex items-center justify-center px-6 py-3 border-2 border-white text-base font-medium rounded-md text-white hover:bg-white hover:text-primary transition-colors"
                    >
                      Đăng nhập
                    </Link>
                  </>
                )}
              </div>
            </div>

            <div className="hidden md:block">
              <div className="relative">
                <div className="absolute inset-0 bg-secondary rounded-lg transform rotate-3"></div>
                <div className="relative bg-white rounded-lg shadow-2xl p-8 transform -rotate-1 hover:rotate-0 transition-transform">
                  {/*<BookOpen className="h-32 w-32 text-primary mx-auto" />*/}
                  <img src={logo} alt="Logo" className="max-h-36 mx-auto" />
                  <p className="text-center text-gray-700 mt-4 font-semibold">
                    Hệ thống quản lý chuyên nghiệp
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Dịch vụ của chúng tôi
            </h2>
            <p className="text-lg text-gray-600 max-w-3xl mx-auto">
              In Phú Thịnh mang đến giải pháp toàn diện cho nhu cầu in ấn và quản lý xuất bản của bạn
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => (
              <div
                key={index}
                className="group p-6 bg-gray-50 rounded-lg hover:bg-primary hover:shadow-xl transition-all duration-300"
              >
                <div className="mb-4 group-hover:scale-110 transition-transform">
                  <div className="inline-block p-3 bg-white rounded-lg shadow-md group-hover:bg-secondary">
                    {feature.icon}
                  </div>
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2 group-hover:text-white">
                  {feature.title}
                </h3>
                <p className="text-gray-600 group-hover:text-primary-100">
                  {feature.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-gradient-to-br from-primary-50 to-secondary-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div className="p-6">
              <div className="text-4xl md:text-5xl font-bold text-primary mb-2">10+</div>
              <p className="text-gray-700 font-medium">Năm kinh nghiệm</p>
            </div>
            <div className="p-6">
              <div className="text-4xl md:text-5xl font-bold text-secondary mb-2">500+</div>
              <p className="text-gray-700 font-medium">Dự án hoàn thành</p>
            </div>
            <div className="p-6">
              <div className="text-4xl md:text-5xl font-bold text-primary mb-2">100%</div>
              <p className="text-gray-700 font-medium">Khách hàng hài lòng</p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-primary text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl md:text-4xl font-bold mb-6">
            Sẵn sàng bắt đầu với In Phú Thịnh?
          </h2>
          <p className="text-lg text-primary-100 mb-8">
            Đăng ký ngay hôm nay để trải nghiệm dịch vụ in ấn và quản lý xuất bản chuyên nghiệp
          </p>
          {!user && (
            <Link
              to="/register"
              className="inline-flex items-center justify-center px-8 py-4 border border-transparent text-lg font-medium rounded-md text-primary bg-white hover:bg-gray-50 transition-colors"
            >
              Đăng ký miễn phí
              <ArrowRight className="ml-2 h-5 w-5" />
            </Link>
          )}
        </div>
      </section>
    </div>
  );
};
